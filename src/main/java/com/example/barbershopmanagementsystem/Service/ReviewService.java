package com.example.barbershopmanagementsystem.Service;

import com.example.barbershopmanagementsystem.Model.*;
import com.example.barbershopmanagementsystem.Repository.*;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final BarberRepository barberRepository;
    private final VoteRepository voteRepository;

    public ReviewService(ReviewRepository reviewRepository, ClientRepository clientRepository, OrderRepository orderRepository, BarberRepository barberRepository, VoteRepository voteRepository) {
        this.reviewRepository = reviewRepository;
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.barberRepository = barberRepository;
        this.voteRepository = voteRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public String updateReview(Integer reviewId, Review review) {
        //Get the review object and check if it's in the database.
        Review tempReview = reviewRepository.findReviewById(reviewId);
        if (tempReview == null) return "Review not found.";

        tempReview.setDescription(review.getDescription());
        tempReview.setRating(review.getRating());
        reviewRepository.save(tempReview);
        return "Review updated successfully.";
    }

    public String deleteReview(Integer reviewId) {
        //Get the review object and check if it's in the database.
        Review tempReview = reviewRepository.findReviewById(reviewId);
        if (tempReview == null) return "Review not found.";

        reviewRepository.delete(tempReview);
        return "Review deleted successfully.";
    }

    //This method is to review an order.
    public String reviewOrder(Integer clientId, Integer orderId, Double rating) {
        //Validate the review.
        String status = validateReview(clientId, orderId, rating);

        //If the status of the validation do not equal "Success" than there is an issue with the review so return the status.
        if (!status.equalsIgnoreCase("Success")) return status;

        //Create the review and save it in the database.
        Review review = createReview(clientId, orderId, rating);
        reviewRepository.save(review);
        Order order = orderRepository.findOrderById(orderId);
        order.setReviewId(review.getId());
        orderRepository.save(order);
        return "Review submitted successfully.";
    }

    //This method act like the above method the only difference is that this review has a description.
    public String reviewOrderWithDescription(Integer clientId, Integer orderId, Double rating, String description) {
        String status = validateReview(clientId, orderId, rating);
        if (!status.equalsIgnoreCase("Success")) return status;
        Review review = createReview(clientId, orderId, rating);
        review.setDescription(description);
        reviewRepository.save(review);
        Order order = orderRepository.findOrderById(orderId);
        order.setReviewId(review.getId());
        orderRepository.save(order);
        return "Review submitted successfully.";
    }

    //This is helper method used to validate a review.
    public String validateReview(Integer clientId, Integer orderId, Double rating) {
        //Get the client object and check if it's in the database.
        Client client = clientRepository.findClientById(clientId);
        if (client == null) return "Client not found.";

        //Get the order object and check if it's in the database.
        Order order = orderRepository.findOrderById(orderId);
        if (order == null) return "Order not found.";

        //Check if the order belong to the client.
        if (!order.getClientId().equals(clientId)) return "The order and client provided do not match.";

        //Only completed orders can be reviewed so check if the order is completed.
        if (!order.getOrderDone()) return "Order need to be completed before submitting a review.";

        //Check if the client has already reviewed this order.
        if (order.getReviewId() != null) return "You can not submit more than one review to one order.";

        //Validate the rating.
        if (rating < 1 || rating > 5) return "The rating should be between 1 and 5.";
        return "Success";
    }

    //This is a helper method to create a review.
    public Review createReview(Integer clientId, Integer orderId, Double rating) {
        Review review = new Review();
        review.setRating(rating);
        review.setClientId(clientId);
        review.setOrderId(orderId);
        return review;
    }

    //This method is used to add a description to an existing review in the database without a description.
    public String addDescriptionToAReview(Integer reviewId, String description) {
        //Get the review and check if it's in the database.
        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) return "Review not found.";

        //Check if the review has a description.
        if (review.getDescription() != null) return "Review already have a description.";
        review.setDescription(description);
        reviewRepository.save(review);
        return "Description added successfully.";
    }

    //Get all the review by a client.
    public List<Review> getReviewsByClientId(Integer clientId) {
        return reviewRepository.findByClientId(clientId);
    }

    //This method used by clients to up vote a review.
    public String upVoteReview(Integer reviewId, Integer clientId) {
        //Get the review and check if it's in the database.
        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) return "Review not found";

        //Get the client and check if it's in the database.
        Client client = clientRepository.findClientById(clientId);
        if (client == null) return "Client not found";

        //Up vote and save the review.
        review.setUpVotes(review.getUpVotes() + 1);
        reviewRepository.save(review);

        //Create the vote and save it.
        Vote vote = new Vote();
        vote.setReviewId(reviewId);
        vote.setClientId(clientId);
        vote.setVote("up");
        voteRepository.save(vote);
        return "Up voted successfully.";
    }

    public String downVoteReview(Integer reviewId, Integer clientId) {
        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) return "Review not found";
        Client client = clientRepository.findClientById(clientId);
        if (client == null) return "Client not found";
        review.setDownVotes(review.getDownVotes() + 1);
        reviewRepository.save(review);
        Vote vote = new Vote();
        vote.setReviewId(reviewId);
        vote.setClientId(clientId);
        vote.setVote("down");
        voteRepository.save(vote);
        return "Down voted successfully.";
    }

    //This method used by the owners to add a reply ro a review.
    public String addReplyToReview(Integer reviewId, String reply) {
        //Get the review and check if it's in the database.
        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) return "Review not found";
        review.setReply(reply);
        reviewRepository.save(review);
        return "Reply added successfully.";
    }

    public String removeReply(Integer reviewId) {
        //Get the review and check if it's in the database.
        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) return "Review not found";
        if (review.getReply() == null) return "This review has no reply.";
        review.setReply(null);
        reviewRepository.save(review);
        return "Reply deleted successfully.";
    }

    //This method calculate a barber average rating.
    public String getBarberAvgRating(Integer barberId) {
        //Get the review and check if it's in the database.
        Barber barber = barberRepository.findBarberById(barberId);
        if (barber == null) return "Barber not found.";

        //Calculate the barber average rating and return it, only if the barber has a rating.
        Double avgRating = calculateBarberAvgRating(reviewRepository.findAll(), barberId);

        //Check if the barber has a rating.
        if (avgRating == null) return barber.getFirstName() + " " + barber.getLastName() + " does not have any rating.";
        return barber.getFirstName() + " " + barber.getLastName() + " has an average rating of: " + avgRating;
    }

    //This method get all the barbers with a rating and return the top-rated barber.
    public String getTopRatedBarber() {
        //A map to store the barbers total ratings.
        HashMap<Integer, Double> barberTotalRatings = new HashMap<Integer, Double>();

        //A map to store the barbers number of ratings.
        HashMap<Integer, Integer> barberNumOfRatings = new HashMap<Integer, Integer>();

        //List to store the barbers ids.
        ArrayList<Integer> barbersId = new ArrayList<Integer>();

        //Get all the reviews in the database.
        List<Review> reviewList = reviewRepository.findAll();

        //Loop the reviews and insert into the maps.
        for (Review review : reviewList) {
            //Get the order object.
            Order order = orderRepository.findOrderById(review.getOrderId());

            //Check if this is a new barber in the loop.
            if (barberTotalRatings.get(order.getBarberId()) == null) {
                //This is a new barber in the loop so initialize it.
                barberTotalRatings.put(order.getBarberId(), review.getRating());
                barberNumOfRatings.put(order.getBarberId(), 1);
                barbersId.add(order.getBarberId());
            } else {
                //This is not a new barber so increase the number of ratings by one and add to the total rating.
                barberTotalRatings.put(order.getBarberId(), barberTotalRatings.get(order.getBarberId()) + review.getRating());
                barberNumOfRatings.put(order.getBarberId(), barberNumOfRatings.get(order.getBarberId()) + 1);
            }

        }

        //Get the top-rated barber.
        Pair<Barber, Double> topBarber = returnTopRatedBarber(barbersId, barberTotalRatings, barberNumOfRatings);

        //Check if the returnTopRatedBarber method returned a rating.
        if (topBarber.b == 0) return "All barbers do not have ratings.";
        return "The top barber is " + topBarber.a.getFirstName() + " " + topBarber.a.getLastName() + " with an average rating of: " + topBarber.b;
    }

    //This is a helper method to get the top-rated barber.
    public Pair<Barber, Double> returnTopRatedBarber(ArrayList<Integer> barbersId, HashMap<Integer, Double> barberTotalRatings, HashMap<Integer, Integer> barberNumOfRatings) {
        //Variables to keep track of the top-rated barber.
        double topBarberRating = 0;
        Integer topBarberId = 0;

        //Loop through the barbers and calculate all the barbers ratings.
        for (Integer barberId : barbersId) {
            double avgRating = barberTotalRatings.get(barberId) / barberNumOfRatings.get(barberId);
            if (topBarberRating < avgRating) {
                topBarberRating = avgRating;
                topBarberId = barberId;
            }
        }
        Barber barber = barberRepository.findBarberById(topBarberId);
        return new Pair<>(barber, topBarberRating);
    }

    //This is a helper method to calculate a barber rating.
    public Double calculateBarberAvgRating(List<Review> reviewList, Integer barberId) {
        Double totalRatings = 0.0;
        int countOfRatings = 0;
        for (Review review : reviewList) {
            Order order = orderRepository.findOrderById(review.getOrderId());
            if (order.getBarberId().equals(barberId)) {
                totalRatings += review.getRating();
                countOfRatings++;
            }

        }
        if (totalRatings == 0) return null;
        return (totalRatings / countOfRatings);
    }

    //This method is for removing a vote
    public String removeVote(Integer voteId, Integer clientId) {
        //Get the client object and check if it's in the database.
        Client client = clientRepository.findClientById(clientId);
        if (client == null) return "Client not found.";

        //Get the vote object and check if it's in the database.
        Vote vote = voteRepository.findVoteById(voteId);
        if (vote == null) return "Vote not found.";

        //Check if the vote was made by the client.
        if (!vote.getClientId().equals(clientId)) return "The vote must be made with the client.";
        Review review = reviewRepository.findReviewById(vote.getReviewId());

        //Check if it's an up vote or down vote and edit the vote.
        if (vote.getVote().equals("up")) review.setUpVotes(review.getUpVotes() - 1);
        else review.setDownVotes(review.getDownVotes() - 1);
        reviewRepository.save(review);

        //Delete the vote from the database
        voteRepository.delete(vote);
        return "Vote removed successfully";
    }

    public String switchVote(Integer voteId, Integer clientId) {
        //Get the client object and check if it's in the database.
        Client client = clientRepository.findClientById(clientId);
        if (client == null) return "Client not found.";

        //Get the vote object and check if it's in the database.
        Vote vote = voteRepository.findVoteById(voteId);
        if (vote == null) return "Vote not found.";

        //Check if the vote was made by the client.
        if (!vote.getClientId().equals(clientId)) return "The vote must be made with the client.";
        Review review = reviewRepository.findReviewById(vote.getReviewId());

        //Check if it's an up vote or down vote and edit the vote.
        if (vote.getVote().equals("up")) {
            review.setUpVotes(review.getUpVotes() - 1);
            review.setDownVotes(review.getDownVotes() + 1);
            vote.setVote("down");
        } else {
            review.setDownVotes(review.getDownVotes() - 1);
            review.setUpVotes(review.getUpVotes() + 1);
            vote.setVote("up");
        }
        //Save the review and the vote.
        reviewRepository.save(review);
        voteRepository.save(vote);
        return "Vote changed successfully.";
    }

    //Get the shop rating.
    public String getShopRating() {
        List<Review> reviewList = reviewRepository.findAll();
        Double totalRatings = 0.0;
        int countOfRatings = 0;
        for (Review review : reviewList) {
            totalRatings += review.getRating();
            countOfRatings++;
        }
        if (totalRatings == 0) return "The shop does not have a rating.";
        return "The shop rating is: " + (totalRatings / countOfRatings);
    }

}