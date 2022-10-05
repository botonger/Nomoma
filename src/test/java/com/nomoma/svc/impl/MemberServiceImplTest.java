package com.nomoma.svc.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.nomoma.member.model.dto.MemberDto;
import com.nomoma.member.model.entity.Member;
import com.nomoma.member.repo.MemberRepository;
import com.nomoma.member.svc.MemberService;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    MemberService service;

    @MockBean
    MemberRepository repository;

    @BeforeEach
    void before() {
        Mockito.when(repository.findAll())
                .thenReturn(List.of(Member.builder().id(1L).name("botonger").build(),
                                    Member.builder().id(2L).name("cheyenne").build()));

        Mockito.when(repository.findMemberByName("zora"))
                .thenReturn(Optional.ofNullable(Member.builder().id(3L).name("zora").build()));
        Mockito.when(repository.findById(3L))
                .thenReturn(Optional.ofNullable(Member.builder().id(3L).name("zora").build()));
        Mockito.when(repository.findById(4L))
                .thenThrow(new RuntimeException("not found"));
    }

    @Test
    void getAllMembers() {
        assertEquals(service.getAllMembers().size(), 2);
    }

    @Test
    void saveMember() {
        MemberDto memberDto = MemberDto.builder()
                .name("zora")
                .build();
        assertThrows(ConstraintViolationException.class, () -> service.saveMember(memberDto));
    }

    @Test
    void updateMember() {
        MemberDto memberDto = MemberDto.builder()
                                       .id(3L)
                                       .name("changed successfully")
                                       .build();
        MemberDto updated = service.updateMember(memberDto);

        assertEquals("changed successfully", updated.getName());
    }

    @Test
    void deleteMember() {
        assertThrows(RuntimeException.class, () -> service.deleteMember(4L));
    }
}
