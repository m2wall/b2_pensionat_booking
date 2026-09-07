package org.example.pensionat_booking.Service;

import org.example.pensionat_booking.DTO.ReviewResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpClient;
import java.util.List;

@Service
public class ReviewServiceClient {

    RestTemplate restTemplate;

    public ReviewServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    public List<ReviewResponseDTO> getAllReviews(){

        try {
            List<ReviewResponseDTO> reviews = restTemplate.getForObject("http://reviews-service/reviews", List.class);
            return reviews;
        } catch (RestClientException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Något gick fel");
        }
    }



}
