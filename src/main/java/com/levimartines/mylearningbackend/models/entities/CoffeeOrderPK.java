package com.levimartines.mylearningbackend.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeOrderPK implements Serializable {

    @Column(name = "coffee_id", nullable = false)
    private Long coffee;

    @Column(name = "order_id", nullable = false)
    private Long order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoffeeOrderPK that = (CoffeeOrderPK) o;
        return Objects.equals(coffee, that.coffee) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coffee, order);
    }
}
