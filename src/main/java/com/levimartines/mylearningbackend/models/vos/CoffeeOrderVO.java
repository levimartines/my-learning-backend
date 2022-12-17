package com.levimartines.mylearningbackend.models.vos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeOrderVO {
    @NotNull
    private List<OrderItemVO> items;
    @NotNull
    @NotEmpty
    private String kartHash;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemVO {
        @NotNull
        private Long id;
        @NotNull
        private Long quantity;
    }
}
