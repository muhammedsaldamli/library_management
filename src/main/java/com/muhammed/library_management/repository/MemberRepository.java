package com.muhammed.library_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muhammed.library_management.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}