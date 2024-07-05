package nus.iss.edu.sg.final_project_backend_resumaid.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import jakarta.servlet.http.HttpServletRequest;
import nus.iss.edu.sg.final_project_backend_resumaid.service.GoogleCalService;
import nus.iss.edu.sg.final_project_backend_resumaid.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/api/cal")
public class GoogleCalRestController {

    @Autowired
    private GoogleCalService googleCalService;

    @Autowired
    private UserService userService;
    

    // GET AVAILABLE TIME SLOTS
    @GetMapping("/available")
    public ResponseEntity<List<Event>> getAvailableBookings(@RequestParam String startDate,
            @RequestParam String endDate, HttpServletRequest request) {
        // DateTime now = new DateTime(System.currentTimeMillis());

        System.out.println(">>>>> START DATE: " + startDate);
        System.out.println(">>>>> END DATE: " + endDate);

        String jwt = userService.getJwtFromHeader(request);

        List<Event> availableBookings = googleCalService.getFutureEvents(startDate, endDate);
        System.out.println(availableBookings);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(availableBookings);
    }

    // BOOK TIME SLOT (EDIT EVENT)
    @PostMapping("/book")
    public String postBooking(@RequestBody String entity) {
        // TODO: process POST request

        return entity;
    }

    // if (items.isEmpty()) {
    // System.out.println("No upcoming events found.");
    // } else {
    // System.out.println("Upcoming events:");
    // for (Event event : items) {
    // System.out.println(">>>>> EVENT: " + event);
    // DateTime start = event.getStart().getDateTime();
    // DateTime end = event.getEnd().getDateTime();

    // // https://google-calendar-simple-api.readthedocs.io/en/latest/colors.html
    // String color = event.getColorId();

    // System.out.printf("[%s] %s (%s - %s)\n", color, event.getSummary(), start,
    // end);
    // }
    // }

    // GET USER BOOKINGS
    @GetMapping("/{userid}")
    public String getBookingsByUser(@PathVariable String userid) {
        // DateTime now = new DateTime(System.currentTimeMillis());
        return new String();
    }
}
