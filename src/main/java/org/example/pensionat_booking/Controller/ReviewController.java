package org.example.pensionat_booking.Controller;

import org.example.pensionat_booking.DTO.ReviewResponseDTO;
import org.example.pensionat_booking.Service.ReviewServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reviews")
public class ReviewController {

    ReviewServiceClient reviewServiceClient;

    public ReviewController(ReviewServiceClient reviewServiceClient) {
        this.reviewServiceClient = reviewServiceClient;
    }

    @GetMapping()
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        try {
            List<ReviewResponseDTO> reviews = reviewServiceClient.getAllReviews();
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}
