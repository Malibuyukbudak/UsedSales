package com.example.application.services;

import com.example.application.models.Message;
import com.example.application.models.Product;
import com.example.application.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MessageServiceImpl implements MessageService{
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @Override
    public List<Message> findAll() {
        return StreamSupport.stream(messageRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public Long count() {
        return messageRepository.count();
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Set<Message> getList(Long id) {
        Set<Message> productSet=new HashSet<>();
        messageRepository.findByUserId(id).iterator().forEachRemaining(productSet::add);
        return productSet;
    }
}
