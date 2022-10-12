package com.nomoma.member.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.nomoma.member.model.entity.Member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {
    private Long id;
    private String name;
    private String password;
    private List<Role> roles;
    private String realm;

    public static MemberDto toDto(Member member) {
        return builder()
                .id(member.getId())
                .name(member.getName())
                .password(member.getPassword())
                .roles(member.getRoles().stream().map(Role::valueOf).collect(Collectors.toList()))
                .realm(member.getRealm())
                .build();
    }

    public static Member toEntity(MemberDto memberDto) {
        return Member.builder()
                .name(memberDto.getName())
                .password(memberDto.getPassword())
                .roles(memberDto.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                .realm(memberDto.getRealm())
                .build();
    }
}
