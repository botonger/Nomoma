package com.nomoma.member.model.dto;

import com.nomoma.member.model.entity.Member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {
    private Long id;
    private String name;

    public static MemberDto toDto(Member member) {
        return builder()
                .id(member.getId())
                .name(member.getName())
                .build();
    }

    public static Member toEntity(MemberDto memberDto) {
        return Member.builder()
                .name(memberDto.getName())
                .build();
    }
}
