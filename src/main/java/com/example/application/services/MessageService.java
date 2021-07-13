package com.example.application.services;

import com.example.application.models.Message;
import com.example.application.models.Product;

import java.util.List;
import java.util.Set;

public interface MessageService {
    List<Message> findAll();

    Long count();

    void delete(Message message);

    void save(Message message);
    Set<Message> getList(Long id);
}
