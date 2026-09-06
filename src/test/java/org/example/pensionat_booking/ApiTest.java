package org.example.pensionat_booking;


import jakarta.validation.Valid;
import org.example.pensionat_booking.DTO.CustomerDTO;
import org.example.pensionat_booking.Model.Booking;
import org.example.pensionat_booking.Model.Room;
import org.example.pensionat_booking.Repository.BookingRepository;
import org.example.pensionat_booking.Repository.RoomRepository;
import org.example.pensionat_booking.Service.BookingService;
import org.example.pensionat_booking.Service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiTest extends MySQLTestContainer {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    CustomerService customerService;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private RoomRepository roomRepo;

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
    void getAllBookings() throws Exception {

        bookingRepo.findAll().forEach(r -> System.out.println(r.getId()));


        mvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void deleteBooking() throws Exception {

        Booking deleteBooking = bookingRepo.findAll().stream().findFirst().orElse(null);

        mvc.perform(delete("/api/bookings/" + deleteBooking.getId()))
                .andExpect(status().isOk());

        mvc.perform(delete("/api/bookings/" + deleteBooking.getId()))
                .andExpect(status().isNotFound());

        mvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

    }




    @Test
    void createAndGetCustomer() throws Exception {
        when(customerService.registerCustomer(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(new CustomerDTO(1L, "Testman", "test@email.com", "076076")));

        mvc.perform(post("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Testman\",\"email\":\"test@email.com\",\"phone\":\"076076\"}"))
                .andExpect(status().isCreated());
    }



//    @Test
//    void createCustomer() throws Exception {
//
//        CustomerDTO testCst = new CustomerDTO(1L, "Test", "Test@email.com", "4444");
//        mvc.perform(post("/api/customers/register", testCst, CustomerDTO.class));
//
//        mvc.perform(get("/api/customers/1"))
//                .andExpect(status().isOk());
//    }


}
