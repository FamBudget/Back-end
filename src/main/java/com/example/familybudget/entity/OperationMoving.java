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
@Table(name = "operations_income")
public class OperationMoving extends BaseEntity {
    @Column(name = "amount")
    private Double amount;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "account_from_id")
    private Account accountFrom;
    @ManyToOne
    @JoinColumn(name = "account_to_id")
    private Account accountTo;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
