package org.example.pensionat_booking;


import org.example.pensionat_booking.Model.Booking;
import org.example.pensionat_booking.Model.Room;
import org.example.pensionat_booking.Repository.BookingRepository;
import org.example.pensionat_booking.Repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest extends MySQLTestContainer {
    @Autowired
    MockMvc mvc;


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Value("${customer-service.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {


        bookingRepo.deleteAll();
        roomRepo.deleteAll();

        LocalDate d1 = LocalDate.of(2026, 5, 18);
        LocalDate d2 = LocalDate.of(2026, 5, 20);
        LocalDate d3 = LocalDate.of(2026, 6, 18);
        LocalDate d4 = LocalDate.of(2026, 6, 20);
        LocalDate d5 = LocalDate.of(2026, 8, 3);
        LocalDate d6 = LocalDate.of(2026, 8, 7);

        Room r1 = roomRepo.save(new Room("A1", true));
        Room r2 = roomRepo.save(new Room("A2", true));
        Room r3 = roomRepo.save(new Room("B3", true));
        Room r4 = roomRepo.save(new Room("B4", true));
        Room r5 = roomRepo.save(new Room("C5", true));
        Room r6 = roomRepo.save(new Room("C6", false));
        Room r7 = roomRepo.save(new Room("D7", false));
        Room r8 = roomRepo.save(new Room("D8", false));
        Room r9 = roomRepo.save(new Room("E9", false));
        Room r10 = roomRepo.save(new Room("E10", false));


        Booking book1 = bookingRepo.save(new Booking(r1, 1L, d1, d2));
        Booking book2 = bookingRepo.save(new Booking(r4, 2L, d3, d4));
        Booking book3 = bookingRepo.save(new Booking(r8, 3L, d5, d6));

        System.out.println(book1.getId() + " " + book2.getId() + " " + book3.getId());


    }

    @Test
    void createAndGetCustomerCreated() throws Exception {

        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);

        server.expect(requestTo(baseUrl + "/customers/register"))
                .andRespond(
                        withStatus(HttpStatus.CREATED)
                                .body("{\"id\": 1, \"name\": \"Testman\", \"email\": \"test@email.com\", \"phone\": \"076076}\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                );

        mvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Testman\",\"email\":\"test@email.com\",\"phone\":\"076076\"}"))
                .andExpect(status().isCreated());



    }

    @Test
    void createAndGetCustomerBadRequest() throws Exception {

        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);

        server.expect(requestTo(baseUrl + "/customers/register"))
                .andRespond(
                        withStatus(HttpStatus.BAD_REQUEST)
                                .body("{\"id\": 1, \"name\": \"Testman\", \"email\": \"test@email.com\", \"phone\": \"07076\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                );

        mvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Testman\",\"email\":\"test@email.com\",\"phone\":\"076076\"}"))
                .andExpect(status().isBadRequest());



    }


}
