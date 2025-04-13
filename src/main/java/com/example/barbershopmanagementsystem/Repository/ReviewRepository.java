package com.example.barbershopmanagementsystem.Repository;

import com.example.barbershopmanagementsystem.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Review findReviewById(Integer reviewId);

    List<Review> findByClientId(Integer clientId);
}