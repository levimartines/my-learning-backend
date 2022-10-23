package com.levimartines.mylearningbackend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levimartines.mylearningbackend.exceptions.ParseException;
import com.levimartines.mylearningbackend.models.messages.OrderMessage;
import com.levimartines.mylearningbackend.models.vos.CoffeeOrderVO;
import com.levimartines.mylearningbackend.security.PrincipalService;
import com.levimartines.mylearningbackend.services.message.MessageService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final MessageService messageService;
    private final ObjectMapper mapper;

    public void addToQueue(CoffeeOrderVO order) {
        if (isInvalid(order)) return;
        sendOrderMessage(order);
    }

    private boolean isInvalid(CoffeeOrderVO order) {
        return order == null || order.getItems().isEmpty();
    }

    private void sendOrderMessage(CoffeeOrderVO order) {
        OrderMessage message = buildMessage(order);
        String jsonMessage;
        try {
            jsonMessage = mapper.writeValueAsString(message);
        } catch (Exception ex) {
            log.error("Error parsing OrderMessage to JSON", ex);
            throw new ParseException(ex);
        }
        messageService.sendMessage(jsonMessage);
    }

    private OrderMessage buildMessage(CoffeeOrderVO order) {
        List<OrderMessage.OrderItem> orderItems = order.getItems().stream()
            .map(item -> new OrderMessage.OrderItem(item.getId(), item.getQuantity()))
            .toList();
        return OrderMessage.builder()
            .user(PrincipalService.getUserId())
            .items(orderItems)
            .build();
    }
}
