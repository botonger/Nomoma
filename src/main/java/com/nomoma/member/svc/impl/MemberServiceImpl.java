package com.nomoma.member.svc.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nomoma.member.model.dto.MemberDto;
import com.nomoma.member.model.entity.Member;
import com.nomoma.member.repo.MemberRepository;
import com.nomoma.member.svc.MemberService;
import com.nomoma.util.KeycloakAdminUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final KeycloakAdminUtil keycloakAdmin;
    private final MemberRepository repository;
    private final BCryptPasswordEncoder encoder;

    //멤버 목록 조회
    @Override
    public List<MemberDto> getAllMembers() {
        return repository.findAll().parallelStream().map(MemberDto::toDto).collect(Collectors.toList());
    }

    //관리자: 멤버 저장
    //로직 분리하기 + role 추가
    @Override
    public MemberDto saveMember(MemberDto memberDto) {
        boolean isSaved = keycloakAdmin.addUser(memberDto);

        if(isSaved) {
            final Optional<Member> member = repository.findMemberByName(memberDto.getName());
            if(member.isPresent()) {
                throw new ConstraintViolationException("Duplicated member", null);
            }
            memberDto.setPassword(encoder.encode(memberDto.getPassword()));
            final Member saved = repository.save(MemberDto.toEntity(memberDto));
            return MemberDto.toDto(saved);

        } else {
            throw new ConstraintViolationException("Duplicated member", null);
        }
    }

    //멤버 이름 변경
    @Transactional
    @Override
    public MemberDto updateMember(MemberDto memberDto) {
        final Member member = repository.findById(memberDto.getId())
                                        .orElseThrow(() -> new NullPointerException("No such member exists"));
        member.changeName(memberDto.getName());
        return MemberDto.toDto(member);
    }

    //멤버 삭제
    @Override
    public void deleteMember(Long id) {
        final Member member = repository.findById(id).orElseThrow(() -> new NullPointerException("No such member exists"));
        repository.delete(member);
    }
}
