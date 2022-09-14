package com.levimartines.mylearningbackend.models.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeOrderPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "coffee_id", nullable = false)
    private Coffee coffee;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}
