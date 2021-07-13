package com.example.application.repository;

import com.example.application.models.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message,Long> {
        List<Message> findByUserId (Long id);
}
