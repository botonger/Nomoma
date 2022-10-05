package com.nomoma.svc.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.nomoma.budget.model.dto.IncomeDto;
import com.nomoma.member.model.dto.MemberDto;
import com.nomoma.budget.model.entity.Income;
import com.nomoma.member.model.entity.Member;
import com.nomoma.budget.repo.IncomeRepository;
import com.nomoma.member.repo.MemberRepository;
import com.nomoma.budget.svc.IncomeService;

@SpringBootTest
class IncomeServiceImplTest {
    @Autowired
    IncomeService service;
    @MockBean
    IncomeRepository repository;
    @MockBean
    MemberRepository memberRepository;
    Member member1, member2;

    @BeforeEach
    void before() {
        member1 = Member.builder().id(1L).name("botonger").build();
        member2 = Member.builder().id(2L).name("zora").build();

        Mockito.when(repository.getIncomesByQuarter("2022-1Q"))
                .thenReturn(List.of(
                        Income.builder().id(1L).quarter("2022-1Q").member(member1).amount(200000).build(),
                        Income.builder().id(3L).quarter("2022-1Q").member(member2).amount(200000).build()
                        ));
        Mockito.when(memberRepository.findById(2L))
                .thenThrow(new RuntimeException("not found"));

        Mockito.when(repository.getIncomeByMember_IdAndQuarter(1L, "2022-1Q"))
                .thenReturn(
                        Optional.ofNullable(Income.builder().id(1L).quarter("2022-1Q").member(member1)
                                                  .amount(200000).build())
                        );
        Mockito.when(repository.getIncomeByMember_IdAndQuarter(2L, "2022-1Q"))
               .thenThrow(new RuntimeException("not found"));
    }

    @Test
    @DisplayName("분기별 팀비 목록 조회 테스트")
    public void getIncomeByQuarterTest() {
        List<IncomeDto> list = service.getIncomesBy("2022-1Q");
        assertEquals(list.size(), 2);
    }

    @Test
    @DisplayName("팀비 저장 테스트")
    public void saveTest() {
        IncomeDto incomeDto = IncomeDto.builder().id(2L).member(MemberDto.toDto(member1)).amount(200000).build();
        assertThrows(RuntimeException.class, () -> memberRepository.findById(2L));
        assertThrows(NullPointerException.class, () -> service.saveIncome(incomeDto));
    }

    @Test
    @DisplayName("팀비 수정 테스트")
    public void updateTest() {
        IncomeDto incomeDto = IncomeDto.builder().id(1L).member(MemberDto.toDto(member1)).quarter("2022-1Q").amount(300000).build();
        assertThrows(RuntimeException.class, () -> repository.getIncomeByMember_IdAndQuarter(2L, "2022-1Q"));

        IncomeDto updated = service.updateIncome(incomeDto);
        assertEquals(updated.getAmount(), 300000);
    }
}
