package com.example.barbershopmanagementsystem.Controller;

import com.example.barbershopmanagementsystem.Api.ApiResponse;
import com.example.barbershopmanagementsystem.Model.Review;
import com.example.barbershopmanagementsystem.Service.HelperService;
import com.example.barbershopmanagementsystem.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/barber-system/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final HelperService helperService;

    @GetMapping("/get-all-reviews")
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getAllReviews());
    }


    @PutMapping("/update-review/{reviewId}")
    public ResponseEntity<ApiResponse> updateReview(@PathVariable Integer reviewId, @RequestBody @Valid Review review, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        return helperService.getResponse(reviewService.updateReview(reviewId, review));
    }

    @DeleteMapping("/delete-review/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Integer reviewId) {
        return helperService.getResponse(reviewService.deleteReview(reviewId));
    }

    @PostMapping("/review-order/{clientId}/{orderId}/{rating}")
    public ResponseEntity<ApiResponse> reviewOrder(@PathVariable Integer clientId, @PathVariable Integer orderId, @PathVariable Double rating) {
        return helperService.getResponse(reviewService.reviewOrder(clientId, orderId, rating));
    }

    //15-
    @PostMapping("/review-order-with-description/{clientId}/{orderId}/{rating}/{description}")
    public ResponseEntity<ApiResponse> reviewOrder(@PathVariable Integer clientId, @PathVariable Integer orderId, @PathVariable Double rating, @PathVariable String description) {
        return helperService.getResponse(reviewService.reviewOrderWithDescription(clientId, orderId, rating, description));
    }

    //16-
    @GetMapping("/get-client-reviews/{clientId}")
    public ResponseEntity<List<Review>> getClientReviews(@PathVariable Integer clientId) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewsByClientId(clientId));
    }

    //17-
    @PutMapping("/add-description-to-a-review/{reviewId}/{description}")
    public ResponseEntity<ApiResponse> addDescriptionToAReview(@PathVariable Integer reviewId, @PathVariable String description) {
        return helperService.getResponse(reviewService.addDescriptionToAReview(reviewId, description));
    }

    //18-
    @PutMapping("/up-vote-review/{reviewId}/{clientId}")
    public ResponseEntity<ApiResponse> upVoteReview(@PathVariable Integer reviewId, @PathVariable Integer clientId) {
        return helperService.getResponse(reviewService.upVoteReview(reviewId, clientId));
    }

    //19-
    @PutMapping("/down-vote-review/{reviewId}/{clientId}")
    public ResponseEntity<ApiResponse> downVoteReview(@PathVariable Integer reviewId, @PathVariable Integer clientId) {
        return helperService.getResponse(reviewService.downVoteReview(reviewId, clientId));
    }

    //20-
    @PutMapping("/switch-vote/{voteId}/{clientId}")
    public ResponseEntity<ApiResponse> switchVote(@PathVariable Integer voteId, @PathVariable Integer clientId){
        return helperService.getResponse(reviewService.switchVote(voteId, clientId));
    }

    //21-
    @PutMapping("/add-reply-to-review/{reviewId}/{reply}")
    public ResponseEntity<ApiResponse> downVoteReview(@PathVariable Integer reviewId, @PathVariable String reply) {
        return helperService.getResponse(reviewService.addReplyToReview(reviewId, reply));
    }

    //22-
    @GetMapping("/get-barber-avg-rating/{barberId}")
    public ResponseEntity<ApiResponse> getBarberAvgRating(@PathVariable Integer barberId) {
        return helperService.getResponse(reviewService.getBarberAvgRating(barberId));
    }

    //23-
    @GetMapping("/get-top-rated-barber")
    public ResponseEntity<ApiResponse> getTopRatedBarber() {
        return helperService.getResponse(reviewService.getTopRatedBarber());
    }

    //24-
    @PutMapping("/remove-vote/{voteId}/{clientId}")
    public ResponseEntity<ApiResponse> removeVote(@PathVariable Integer voteId, @PathVariable Integer clientId){
        return helperService.getResponse(reviewService.removeVote(voteId, clientId));
    }

    //25-
    @PutMapping("/remove-reply/{reviewId}")
    public ResponseEntity<ApiResponse> removeReply(@PathVariable Integer reviewId){
        return helperService.getResponse(reviewService.removeReply(reviewId));
    }

    //26-
    @GetMapping("/get-shop-rating")
    public ResponseEntity<ApiResponse> getShopRating(){
        return helperService.getResponse(reviewService.getShopRating());
    }

}