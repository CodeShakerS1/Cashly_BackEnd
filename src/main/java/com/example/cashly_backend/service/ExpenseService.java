    package com.example.cashly_backend.service;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.util.List;
    import java.util.Optional;

    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import com.example.cashly_backend.entity.Expense;
    import com.example.cashly_backend.entity.Transaction;
    import com.example.cashly_backend.repository.ExpenseRepository;
    import com.example.cashly_backend.repository.TransactionRepository;

    @Service
    public class ExpenseService {

        private final ExpenseRepository repository;

        private final TransactionRepository transactionRepository;

        private final NotificationTriggerService notificationTriggerService;

        private final CategoryService categoryService;

        ExpenseService(ExpenseRepository repository, TransactionRepository transactionRepository,NotificationTriggerService notificationTriggerService, CategoryService categoryService) {
            this.repository = repository;
            this.transactionRepository = transactionRepository;
            this.notificationTriggerService = notificationTriggerService;
            this.categoryService = categoryService;
        }

        public List<Expense> findAll() {
            return repository.findAll();
        }

        public Optional<Expense> findById(Integer id) {
            return repository.findById(id);
        }

        public List<Expense> findByUser(Integer id) {
            return repository.findByUserId(id);
        }

        public List<Expense> findByPeriod(Integer id, LocalDate start, LocalDate end) {
            return repository.findByUserIdAndDateBetween(id, start, end);
        }

        public BigDecimal sumByPeriod(Integer id, LocalDate start, LocalDate end) {
            BigDecimal total = repository.sumByUserAndPeriod(id, start, end);
            return total != null ? total : BigDecimal.ZERO;
        }

        public List<Expense> findByCategory(Integer userId, Integer categoryId) {
            return repository.findByUserIdAndCategoryId(userId, categoryId);
        }

        public Expense save(Expense expense) {
            Expense saved = repository.save(expense);

            Transaction transaction = new Transaction();
            transaction.setAmount(saved.getAmount());
            transaction.setDescription(saved.getName());
            transaction.setMethod(saved.getMethod());
            transaction.setDate(saved.getDate());
            transaction.setUserId(saved.getUserId());
            transaction.setExpenseId(saved.getId());

            transactionRepository.save(transaction);

            if (saved.getCategoryId() != null) {
                categoryService.findById(saved.getCategoryId()).ifPresent(category ->
                    notificationTriggerService.checkCategoryLimit(category, saved.getUserId()));
                }
                notificationTriggerService.notifyExpenseAdded(saved.getName(), saved.getAmount(), saved.getUserId());
                notificationTriggerService.checkNegativeBalance(saved.getUserId());

            return saved;
        }

        public Expense update(Integer id, Expense updatedExpense) {
            Expense existing = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Expense not found"));

            existing.setName(updatedExpense.getName());
            existing.setAmount(updatedExpense.getAmount());
            existing.setDate(updatedExpense.getDate());
            existing.setMethod(updatedExpense.getMethod());
            existing.setCategoryId(updatedExpense.getCategoryId());

            Expense saved = repository.save(existing);

            transactionRepository.findByExpenseId(id).ifPresent(transaction -> {
                transaction.setAmount(saved.getAmount());
                transaction.setDescription(saved.getName());
                transaction.setMethod(saved.getMethod());
                transaction.setDate(saved.getDate());
                transactionRepository.save(transaction);
            });

            notificationTriggerService.notifyExpenseUpdated(saved.getName(), saved.getAmount(), saved.getUserId());
            notificationTriggerService.checkNegativeBalance(saved.getUserId());

            return saved;
    }

        @Transactional
        public void delete(Integer id) {
            transactionRepository.deleteByExpenseId(id);
            repository.deleteById(id);
        }
    }