package com.fitness.fitness_center.service;

import com.fitness.fitness_center.model.Member;
import com.fitness.fitness_center.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
    
    public Optional<Member> getMemberById(Integer id) {
        return memberRepository.findById(id);
    }
    
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
    
    public void deleteMember(Integer id) {
        memberRepository.deleteById(id);
    }
    
    public List<Member> searchMembersByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Member> getMembersByMembershipType(String type) {
        return memberRepository.findByMembershipType(type);
    }
    
    public long getTotalMembers() {
        return memberRepository.countMembers();
    }
    
    public long getMembersByTypeCount(String type) {
        return memberRepository.countByMembershipType(type);
    }
}