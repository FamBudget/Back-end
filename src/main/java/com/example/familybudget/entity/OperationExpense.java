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
@Table(name = "operations_expense")
public class OperationExpense extends BaseEntity {
    @Column(name = "amount")
    private Double amount;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryExpense category;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
