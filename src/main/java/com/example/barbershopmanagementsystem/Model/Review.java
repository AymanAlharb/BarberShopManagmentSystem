package com.example.barbershopmanagementsystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(100)")
    private String description;
    @NotNull(message = "The rating can not be null.")
    @Min(value = 1, message = "The minimum rating is 1.")
    @Max(value = 5, message = "The maximum rating is 5.")
    @Column(columnDefinition = "int not null")
    private Double rating;
    @PositiveOrZero
    @Column(columnDefinition = "int")
    private Integer upVotes = 0;
    @PositiveOrZero
    @Column(columnDefinition = "int")
    private Integer downVotes = 0;
    @Column(columnDefinition = "varchar(100)")
    //Owners Reply to the review.
    private String reply;
    @Column(columnDefinition = "int")
    private Integer clientId;
    @Column(columnDefinition = "int")
    private Integer orderId;
}
