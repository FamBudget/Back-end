package com.example.familybudget.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "amount")
    private Float amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
