package com.example.cashly_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cashly_backend.entity.Notification;
import com.example.cashly_backend.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public List<Notification> listarTodas() {
        return repository.findAll();
    }

    public Optional<Notification> listarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Notification> listarPorUsuario(Integer userId) {
        return repository.findByUserId(userId);
    }

    public List<Notification> listarNaoLidasPorUsuario(Integer userId) {
        return repository.findByUserIdAndIsRead(userId, false);
    }

    public Notification cadastrar(Notification notification) {
        return repository.save(notification);
    }

    public Notification criarNotificacao(String titulo, String mensagem, String tipo, Integer userId) {
        Notification notif = new Notification();
        notif.setTitle(titulo);
        notif.setMessage(mensagem);
        notif.setType(tipo);
        notif.setUserId(userId);
        notif.setIsRead(false);
        notif.setCreatedAt(LocalDateTime.now());
        
        return this.cadastrar(notif);
    }

    public Notification marcarComoLida(Integer id) {
    Notification notif = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
    notif.setIsRead(true);
    return repository.save(notif); 
}

    public void deletar(Integer id) {
        repository.deleteById(id);
    }
}
