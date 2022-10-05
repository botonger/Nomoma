package com.nomoma.budget.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.nomoma.member.model.entity.Member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ExpenseUsers {
    @OneToMany
    @Column(nullable = false)
    private List<Member> expenseUsers = new ArrayList<>();

    public void addUser(Member member) {
        expenseUsers.add(member);
    }
}
