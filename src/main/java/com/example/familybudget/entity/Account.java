package com.example.familybudget.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "start_amount")
    private Double startAmount;
    @Column(name = "amount")
    private Double amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
