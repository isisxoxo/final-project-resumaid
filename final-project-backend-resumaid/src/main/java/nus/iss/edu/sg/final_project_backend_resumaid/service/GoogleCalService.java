package nus.iss.edu.sg.final_project_backend_resumaid.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;

import nus.iss.edu.sg.final_project_backend_resumaid.exceptions.GoogleCalException;
import nus.iss.edu.sg.final_project_backend_resumaid.exceptions.UserExistsException;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Booking;
import nus.iss.edu.sg.final_project_backend_resumaid.model.User;
import nus.iss.edu.sg.final_project_backend_resumaid.repository.GoogleCalRepo;
import nus.iss.edu.sg.final_project_backend_resumaid.repository.UserRepo;

@Service
public class GoogleCalService {

    /* NEW METHOD: API KEY */
    // @Value("${googlecal.api.key}")
    // private String apiKey;

    // private String CALENDAR_ID = "resumaidtfip@gmail.com";

    // private String GOOGLE_CAL_URL =
    // "https://www.googleapis.com/calendar/v3/calendars/" + CALENDAR_ID;

    // // GET ALL AVAILABLE TIME SLOTS
    // public List<Event> getFutureEvents(String startDate, String endDate) throws
    // GoogleCalException {

    // System.out.println("IN GET FUTURE EVENTS");
    // Events events;

    // try {
    // DateTime fromDateTime = DateTime.parseRfc3339(startDate);
    // DateTime toDateTime = DateTime.parseRfc3339(endDate);

    // String url = UriComponentsBuilder
    // .fromUriString(GOOGLE_CAL_URL)
    // .queryParam("key", apiKey)
    // .toUriString();

    // // BUILD REQUEST
    // RequestEntity<Void> req = RequestEntity.get(url).build();

    // // GET RESPONSE
    // RestTemplate template = new RestTemplate();
    // ResponseEntity<Calendar> resp = template.exchange(req, Calendar.class);
    // Calendar calendar = resp.getBody();

    // events = calendar.events().list(CALENDAR_ID)
    // .setMaxResults(20)
    // .setTimeMin(fromDateTime)
    // .setTimeMax(toDateTime)
    // .setOrderBy("startTime")
    // .setSingleEvents(true)
    // .execute();
    // } catch (Exception e) {
    // throw new GoogleCalException("Cannot find Google Calendar" + " " +
    // e.getMessage());
    // }

    // List<Event> items = events.getItems();

    // return items;
    // }

    @Autowired
    private Calendar calendar;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GoogleCalRepo googleCalRepo;

    private String CALENDAR_ID = "resumaidtfip@gmail.com";

    // GET ALL AVAILABLE TIME SLOTS
    public List<Event> getFutureEvents(String startDate, String endDate) throws GoogleCalException {
        Events events;

        try {
            DateTime fromDateTime = DateTime.parseRfc3339(startDate);
            DateTime toDateTime = DateTime.parseRfc3339(endDate);

            events = calendar.events().list(CALENDAR_ID)
                    .setMaxResults(20)
                    .setTimeMin(fromDateTime)
                    .setTimeMax(toDateTime)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (Exception e) {
            throw new GoogleCalException("Cannot find Google Calendar");
        }

        // Only filter for available time slots (colorid = 2)
        List<Event> items = events.getItems().stream().filter(i -> i.getColorId().equals("2")).toList();

        return items;
    }

    // SET BOOKING (Google Calendar + User)
    public Boolean setBooking(String id, String userid) throws GoogleCalException, UserExistsException {

        Optional<User> user = userRepo.getUserById(userid);

        Integer setBookingResult = 0;

        if (user.isPresent()) {

            try {

                // Save to Google Calendar
                Event existingEvent = calendar.events().get(CALENDAR_ID, id).execute();
                System.out.println(">>>>> EXISTING EVENT: " + existingEvent);

                List<EventAttendee> listAttendees = existingEvent.getAttendees();
                System.out.println("LIST OF ATTENDEES: " + listAttendees);
                // if (listAttendees == null) { //Requires Domain-wide Delegation (not possible)
                // listAttendees = new ArrayList<>();
                // }
                // EventAttendee newAttendee = new
                // EventAttendee().setEmail(user.get().getEmail());

                // listAttendees.add(newAttendee);

                // existingEvent.setAttendees(listAttendees); // Add new attendee (customer)

                existingEvent.setColorId("11"); // Change from green to red

                Event updatedEvent = calendar.events().update(CALENDAR_ID, id, existingEvent).execute();

                // Save for user
                Booking newBooking = new Booking();
                newBooking.setId(id);
                newBooking.setUserid(userid);
                newBooking.setStarttime(updatedEvent.getStart().getDateTime());
                newBooking.setEndtime(updatedEvent.getEnd().getDateTime());
                newBooking.setMeetinglink(updatedEvent.getHangoutLink());

                setBookingResult = googleCalRepo.saveBooking(newBooking);

            } catch (Exception e) {
                // throw new GoogleCalException("Cannot update Google Calendar");
                throw new GoogleCalException("Cannot update Google Calendar" + " " +
                        e.getMessage());
            }
        } else {
            throw new UserExistsException("No such user id found");
        }

        return setBookingResult > 0 ? true : false;
    }

    // GET BOOKINGS FOR USER
    public List<Booking> getBookingsByUserid(String userid) {
        return googleCalRepo.getBookingsByUserid(userid);
    }
}