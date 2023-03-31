package com.example.familybudget.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "categories_expense")
public class CategoryExpense extends BaseEntity {
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
