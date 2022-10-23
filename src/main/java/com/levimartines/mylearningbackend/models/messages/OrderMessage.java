package com.levimartines.mylearningbackend.models.messages;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderMessage {

    private Long user;
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class OrderItem {
        private Long coffee;
        private Long quantity;
    }
}
