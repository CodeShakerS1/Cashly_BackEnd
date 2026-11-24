package com.example.cashly_backend.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.cashly_backend.dto.ImportResult;
import com.example.cashly_backend.entity.Category;
import com.example.cashly_backend.entity.Expense;
import com.example.cashly_backend.repository.CategoryRepository;

@Service
public class SpreadsheetService {

    private static final DateTimeFormatter BR_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // texto da planilha (sem acento, minúsculo) -> valor que o banco usa
    private static final Map<String, String> METHODS = Map.of(
            "pix", "Pix",
            "debito", "Debit", "debit", "Debit",
            "credito", "Credit", "credit", "Credit",
            "dinheiro", "Cash", "cash", "Cash");

    // valor do banco -> texto em português para exportar
    private static final Map<String, String> METHOD_LABELS = Map.of(
            "Pix", "Pix", "Debit", "Débito", "Credit", "Crédito", "Cash", "Dinheiro");

    private final ExpenseService expenseService;
    private final CategoryRepository categoryRepository;

    SpreadsheetService(ExpenseService expenseService, CategoryRepository categoryRepository) {
        this.expenseService = expenseService;
        this.categoryRepository = categoryRepository;
    }

    // =====================================================
    // IMPORTAR
    // =====================================================

    public ImportResult importExpenses(MultipartFile file, Integer userId) throws IOException {
        String filename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();

        List<Map<String, String>> rows;
        if (filename.endsWith(".csv")) {
            rows = readCsv(file.getInputStream());
        } else if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {
            rows = readExcel(file.getInputStream());
        } else {
            throw new IllegalArgumentException("Envie um arquivo .xlsx ou .csv");
        }

        // nome da categoria -> id, só das categorias do próprio usuário (são as que o app mostra)
        Map<String, Integer> categoryIds = new HashMap<>();
        for (Category c : categoryRepository.findByUserId(userId)) {
            categoryIds.put(normalize(c.getCategoryname()), c.getCategoryid());
        }

        int imported = 0;
        List<String> errors = new ArrayList<>();
        List<String> createdCategories = new ArrayList<>();

        for (Map<String, String> row : rows) {
            String line = row.get("_linha");
            try {
                String name = row.getOrDefault("nome", "").trim();
                if (name.isEmpty()) throw new IllegalArgumentException("nome vazio");

                BigDecimal amount = parseAmount(row.get("valor"));

                String method = METHODS.get(normalize(row.getOrDefault("metodo", "")));
                if (method == null) throw new IllegalArgumentException("método inválido");

                LocalDate date = parseDate(row.get("data"));

                // categoria é opcional; se o nome ainda não existir para o usuário, cria a categoria
                Integer categoryId = null;
                String categoryName = row.getOrDefault("categoria", "").trim();
                if (!categoryName.isEmpty()) {
                    String key = normalize(categoryName);
                    categoryId = categoryIds.get(key);

                    if (categoryId == null) {
                        String finalName = categoryName.length() > 50 ? categoryName.substring(0, 50) : categoryName;
                        Category created = categoryRepository.save(new Category(null, finalName, "category", null, userId));
                        categoryId = created.getCategoryid();
                        categoryIds.put(key, categoryId);
                        createdCategories.add(finalName);
                    }
                }

                expenseService.save(new Expense(null, name, amount, method, date, userId, categoryId));
                imported++;
            } catch (Exception e) {
                errors.add("Linha " + line + ": " + e.getMessage());
            }
        }

        return new ImportResult(imported, errors, createdCategories);
    }

    // Lê um .xlsx/.xls. Devolve uma lista de linhas; cada linha é um mapa cabeçalho -> valor
    private List<Map<String, String>> readExcel(InputStream in) throws IOException {
        List<Map<String, String>> rows = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (Workbook workbook = WorkbookFactory.create(in)) {
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(sheet.getFirstRowNum());
            if (headerRow == null) return rows;

            int cols = headerRow.getLastCellNum();
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < cols; i++) {
                headers.add(normalize(formatter.formatCellValue(headerRow.getCell(i))));
            }

            for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Map<String, String> map = new LinkedHashMap<>();
                boolean empty = true;
                for (int i = 0; i < cols; i++) {
                    String value = cellText(row.getCell(i), formatter, evaluator);
                    if (!value.isEmpty()) empty = false;
                    map.put(headers.get(i), value);
                }
                if (empty) continue; // pula linha em branco

                map.put("_linha", String.valueOf(r + 1)); // número da linha como aparece no Excel
                rows.add(map);
            }
        }
        return rows;
    }

    private String cellText(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate().toString(); // aaaa-mm-dd
            }
            return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
        }
        return formatter.formatCellValue(cell, evaluator).trim();
    }

    // Lê um .csv: detecta o separador (; , ou tab), a codificação (UTF-8 ou ANSI do Excel)
    // e aceita valores entre aspas, como "152,90".
    private List<Map<String, String>> readCsv(InputStream in) throws IOException {
        List<Map<String, String>> rows = new ArrayList<>();

        String text = decode(in.readAllBytes()).replace("\uFEFF", ""); // tira o BOM
        String[] lines = text.split("\\r?\\n");
        if (lines.length == 0 || lines[0].isBlank()) return rows;

        char separator = detectSeparator(lines[0]);

        List<String> headers = new ArrayList<>();
        for (String h : splitCsvLine(lines[0], separator)) {
            headers.add(normalize(h));
        }

        for (int n = 1; n < lines.length; n++) {
            if (lines[n].isBlank()) continue;

            List<String> values = splitCsvLine(lines[n], separator);
            Map<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                map.put(headers.get(i), i < values.size() ? values.get(i).trim() : "");
            }
            map.put("_linha", String.valueOf(n + 1));
            rows.add(map);
        }
        return rows;
    }

    // Tenta UTF-8; se o arquivo não for UTF-8 (CSV "ANSI" do Excel), usa windows-1252
    private String decode(byte[] bytes) {
        try {
            return StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes))
                    .toString();
        } catch (CharacterCodingException e) {
            return new String(bytes, Charset.forName("windows-1252"));
        }
    }

    // Separador = o que mais aparece na linha do cabeçalho
    private char detectSeparator(String header) {
        char best = ';';
        long bestCount = 0;
        for (char c : new char[] { ';', ',', '\t' }) {
            final char candidate = c;
            long count = header.chars().filter(ch -> ch == candidate).count();
            if (count > bestCount) {
                best = c;
                bestCount = count;
            }
        }
        return best;
    }

    // Divide uma linha respeitando aspas: "152,90";Pix -> [152,90] [Pix]
    private List<String> splitCsvLine(String line, char separator) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"'); // aspas duplas escapadas
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == separator && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        values.add(current.toString());
        return values;
    }

    private BigDecimal parseAmount(String value) {
        String s = value == null ? "" : value.replace("R$", "").replace(" ", "").trim();
        if (s.isEmpty()) throw new IllegalArgumentException("valor vazio");

        // "1.234,50" -> "1234.50"
        if (s.contains(",")) s = s.replace(".", "").replace(",", ".");

        try {
            BigDecimal amount = new BigDecimal(s).setScale(2, RoundingMode.HALF_UP);
            if (amount.signum() <= 0) throw new IllegalArgumentException("valor deve ser maior que zero");
            return amount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("valor inválido (" + value + ")");
        }
    }

    private LocalDate parseDate(String value) {
        String s = value == null ? "" : value.trim();
        if (s.isEmpty()) throw new IllegalArgumentException("data vazia");
        try {
            return LocalDate.parse(s); // aaaa-mm-dd
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(s, BR_DATE); // dd/mm/aaaa
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("data inválida (" + value + ")");
            }
        }
    }

    // =====================================================
    // EXPORTAR
    // =====================================================

    public byte[] exportExpenses(Integer userId, String format) throws IOException {
        List<Expense> expenses = expenseService.findByUser(userId);

        Map<Integer, String> categoryNames = new HashMap<>();
        for (Category c : loadCategories(userId)) {
            categoryNames.put(c.getCategoryid(), c.getCategoryname());
        }

        return format.equalsIgnoreCase("csv")
                ? buildCsv(expenses, categoryNames)
                : buildExcel(expenses, categoryNames);
    }

    private byte[] buildExcel(List<Expense> expenses, Map<Integer, String> categoryNames) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Despesas");

            // estilos: cabeçalho em negrito, valor em R$ e data de verdade (não texto)
            Font bold = workbook.createFont();
            bold.setBold(true);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(bold);

            DataFormat dataFormat = workbook.getCreationHelper().createDataFormat();
            CellStyle moneyStyle = workbook.createCellStyle();
            moneyStyle.setDataFormat(dataFormat.getFormat("\"R$\" #,##0.00"));
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(dataFormat.getFormat("dd/mm/yyyy"));

            String[] titles = { "Nome", "Valor", "Método", "Data", "Categoria" };
            Row header = sheet.createRow(0);
            for (int i = 0; i < titles.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(titles[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 5000);
            }

            int r = 1;
            for (Expense e : expenses) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(e.getName());

                Cell amount = row.createCell(1);
                amount.setCellValue(e.getAmount().doubleValue());
                amount.setCellStyle(moneyStyle);

                row.createCell(2).setCellValue(METHOD_LABELS.getOrDefault(e.getMethod(), e.getMethod()));

                Cell date = row.createCell(3);
                date.setCellValue(e.getDate());
                date.setCellStyle(dateStyle);

                row.createCell(4).setCellValue(categoryNames.getOrDefault(e.getCategoryId(), ""));
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] buildCsv(List<Expense> expenses, Map<Integer, String> categoryNames) {
        StringBuilder csv = new StringBuilder("\uFEFF"); // BOM: o Excel mostra os acentos certo
        csv.append("Nome;Valor;Método;Data;Categoria\n");

        for (Expense e : expenses) {
            csv.append(clean(e.getName())).append(';')
               .append(e.getAmount().toPlainString().replace('.', ',')).append(';')
               .append(METHOD_LABELS.getOrDefault(e.getMethod(), e.getMethod())).append(';')
               .append(e.getDate().format(BR_DATE)).append(';')
               .append(clean(categoryNames.getOrDefault(e.getCategoryId(), ""))).append('\n');
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    // tira ; e quebras de linha do texto para não quebrar o CSV
    private String clean(String text) {
        return text == null ? "" : text.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }

    // =====================================================
    // AUXILIARES
    // =====================================================

    private List<Category> loadCategories(Integer userId) {
        List<Category> all = new ArrayList<>(categoryRepository.findByUserIdIsNull());
        all.addAll(categoryRepository.findByUserId(userId));
        return all;
    }

    // "Método" -> "metodo" (sem acento, minúsculo) para comparar sem erro
    private String normalize(String text) {
        if (text == null) return "";
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase();
    }
}