package com.fitness.fitness_center.repository;

import com.fitness.fitness_center.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    
    List<Member> findByMembershipType(String membershipType);
    
    List<Member> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT COUNT(m) FROM Member m")
    long countMembers();
    
    @Query("SELECT COUNT(m) FROM Member m WHERE m.membershipType = ?1")
    long countByMembershipType(String membershipType);
    
    @Query("SELECT m FROM Member m WHERE m.trainerId = ?1")
    List<Member> findByTrainerId(Integer trainerId);
}