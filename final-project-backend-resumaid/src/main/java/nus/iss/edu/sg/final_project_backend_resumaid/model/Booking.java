package nus.iss.edu.sg.final_project_backend_resumaid.model;

import com.google.api.client.util.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    private String id;
    private String userid;
    private DateTime starttime;
    private DateTime endtime;
    private String meetinglink;
}
