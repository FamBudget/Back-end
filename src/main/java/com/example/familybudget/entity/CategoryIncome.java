package com.example.familybudget.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories_income")
public class CategoryIncome extends BaseEntity {
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
