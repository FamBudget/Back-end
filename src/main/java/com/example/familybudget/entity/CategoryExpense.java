package com.example.familybudget.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories_expense")
public class CategoryExpense extends BaseEntity {
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "icon_number")
    private Long iconNumber;
}
