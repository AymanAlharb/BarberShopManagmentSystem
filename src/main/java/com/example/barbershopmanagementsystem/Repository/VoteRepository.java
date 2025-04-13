package com.example.barbershopmanagementsystem.Repository;

import com.example.barbershopmanagementsystem.Model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Vote findVoteById(Integer voteId);
}
