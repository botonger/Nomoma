package com.nomoma.member.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nomoma.member.model.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Override
    List<Member> findAll();

    Optional<Member> findMemberByName(String name);

    @Override
    Member save(Member member);

    @Modifying
    @Query(value = "UPDATE Member m set m.name = :name where m.id = :id")
    int update(@Param("id") Long id, @Param("name") String name);

    @Override
    @Modifying
    void delete(Member member);
}
