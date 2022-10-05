package com.nomoma.member.svc;

import java.util.List;

import com.nomoma.member.model.dto.MemberDto;

public interface MemberService {
    List<MemberDto> getAllMembers();
    MemberDto saveMember(MemberDto member);
    MemberDto updateMember(MemberDto member);
    void deleteMember(Long id);
}
