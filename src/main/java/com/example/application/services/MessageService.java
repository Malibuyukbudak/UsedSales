package com.example.application.services;

import com.example.application.models.Message;
import java.util.List;

public interface MessageService {
    List<Message> findAll();

    Long count();

    void delete(Message message);

    void save(Message message);
}
