package com.nomoma.budget.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expenses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String quarter;

    @Basic
    private LocalDateTime dateTime;

    @PositiveOrZero
    private int amount;

    private String place;

    private String content;

    private String type;

    @Embedded
    private ExpenseUsers expenseUsers = new ExpenseUsers();

    @PrePersist
    public void prePersist() {
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }
    }
}
