package nus.iss.edu.sg.final_project_backend_resumaid.repository;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.google.api.client.util.DateTime;

import nus.iss.edu.sg.final_project_backend_resumaid.model.Booking;
import nus.iss.edu.sg.final_project_backend_resumaid.model.User;
import nus.iss.edu.sg.final_project_backend_resumaid.utils.Queries;

@Repository
public class UserRepo implements Queries {

    @Autowired
    JdbcTemplate template;

    // Check if email already exists
    public Boolean emailExist(String email) {

        Boolean isEmailExist = false;

        SqlRowSet rs = template.queryForRowSet(COUNT_USER_BY_EMAIL, email);
        if (rs.next()) {
            int x = rs.getInt("count");
            if (x > 0) {
                isEmailExist = true;
            }
        }
        return isEmailExist;
    }

    // Insert new user (Registration)
    public String saveUser(User newUser) {

        String id = UUID.randomUUID().toString().substring(0, 8);

        template.update(INSERT_USER, id, newUser.getUsername(), newUser.getEmail(), newUser.getPassword());

        return id;
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {

        SqlRowSet rs = template.queryForRowSet(GET_USER_BY_EMAIL, email);
        if (!rs.next()) {
            return Optional.empty();
        }
        User user = new User();
        user.setId(rs.getString("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(email);
        user.setPassword(rs.getString("password"));
        return Optional.of(user);
    }

    // Get user by id
    public Optional<User> getUserById(String id) {

        SqlRowSet rs = template.queryForRowSet(GET_USER_BY_ID, id);
        if (!rs.next()) {
            return Optional.empty();
        }
        User user = new User();
        user.setId(id);
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        return Optional.of(user);
    }

    /* GOOGLE CAL */
    // Insert new booking (Google Calendar)
    public Integer saveBooking(Booking newBooking) throws ParseException {

        // DateTime -> Timestamp -> Formatted DateTime
        DateTime startTime = newBooking.getStarttime();
        DateTime endTime = newBooking.getEndtime();

        Timestamp startTimestamp = new Timestamp(startTime.getValue());
        Timestamp endTimestamp = new Timestamp(endTime.getValue());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Integer insertCount = template.update(INSERT_BOOKING, newBooking.getId(), newBooking.getUserid(),
                sdf.format(startTimestamp), sdf.format(endTimestamp), newBooking.getMeetinglink());

        return insertCount;
    }

    // Get all bookings for specific user by userid
    public List<Booking> getBookingsByUserid(String userid) {

        List<Booking> bookings = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(GET_BOOKINGS_BY_USERID, userid);

        while (rs.next()) {
            Booking booking = new Booking();
            booking.setId(rs.getString("id"));
            booking.setUserid(rs.getString("userid"));
            booking.setStarttime(DateTime.parseRfc3339(rs.getString("starttime") + ":00.0000+08:00"));
            booking.setEndtime(DateTime.parseRfc3339(rs.getString("endtime") + ":00.0000+08:00"));
            booking.setMeetinglink(rs.getString("meetinglink"));
            bookings.add(booking);
        }
        return bookings;
    }
}
