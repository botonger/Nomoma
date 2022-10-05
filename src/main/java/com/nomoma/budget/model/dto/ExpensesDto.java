package com.nomoma.budget.model.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.nomoma.budget.model.entity.ExpenseUsers;
import com.nomoma.member.model.entity.Member;
import com.nomoma.budget.model.entity.Expenses;
import com.nomoma.member.repo.MemberRepository;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class ExpensesDto {
    private Long id;
    private String quarter;
    private LocalDateTime dateTime;
    private int amount;
    private String place;
    private String content;
    private String type;
    private List<String> members;

    public static ExpensesDto toDto(Expenses expenses) {
        return builder()
                .id(expenses.getId())
                .quarter(expenses.getQuarter())
                .dateTime(expenses.getDateTime())
                .amount(expenses.getAmount())
                .place(expenses.getPlace())
                .content(expenses.getContent())
                .type(expenses.getType())
                .members(expenses.getExpenseUsers().getExpenseUsers()
                                .stream().map(Member::getName)
                                .collect(Collectors.toList()))
                .build();
    }

    public static Expenses toEntity(ExpensesDto expensesDto, MemberRepository repository) {
        final ExpenseUsers expenseUsers = new ExpenseUsers();
        expensesDto.getMembers()
                  .forEach(name -> repository.findMemberByName(name)
                                             .ifPresentOrElse(
                                                     expenseUsers::addUser,
                                                     () -> {throw new NullPointerException("No such member exists");}
                                             ));

        return Expenses.builder()
                       .quarter(expensesDto.getQuarter())
                       .dateTime(expensesDto.getDateTime())
                       .amount(expensesDto.getAmount())
                       .place(expensesDto.getPlace())
                       .content(expensesDto.getContent())
                       .type(expensesDto.getType())
                       .expenseUsers(expenseUsers)
                       .build();
    }

    public static List<String> getTitles() {
        return List.of("no", "type", "dateTime", "content", "place", "amount", "quarter", "expenseUsers");
    }

    public List<Object> getBody(int i) {
        return List.of(i, type, dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), content, place, amount, quarter, members.stream().collect(Collectors.joining(", ")));
    }
}
