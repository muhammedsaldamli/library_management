package com.muhammed.library_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.muhammed.library_management.entity.Member;
import com.muhammed.library_management.exception.MemberNotFoundException;
import com.muhammed.library_management.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class MemberService {

    private final MemberRepository memberRepository;

    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Üye bulunamadı, id: " + id));
    }
}