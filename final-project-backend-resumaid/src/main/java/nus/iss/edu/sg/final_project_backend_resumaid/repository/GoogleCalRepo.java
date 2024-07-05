package nus.iss.edu.sg.final_project_backend_resumaid.repository;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.google.api.client.util.DateTime;

import nus.iss.edu.sg.final_project_backend_resumaid.model.Booking;
import nus.iss.edu.sg.final_project_backend_resumaid.utils.Queries;

@Repository
public class GoogleCalRepo implements Queries {

    @Autowired
    JdbcTemplate template;

    // Insert new booking (Google Calendar)
    public Integer saveBooking(Booking newBooking) {

        Integer insertCount = template.update(INSERT_BOOKING, newBooking.getId(), newBooking.getUserid(),
                newBooking.getStarttime(), newBooking.getEndtime());

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
            booking.setStarttime((DateTime) rs.getObject("starttime"));
            booking.setEndtime((DateTime) rs.getObject("endtime"));
            bookings.add(booking);
        }
        return bookings;
    }
}
