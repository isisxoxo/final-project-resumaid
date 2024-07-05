package nus.iss.edu.sg.final_project_backend_resumaid.utils;

public interface Queries {

        // C (user)
        public static final String INSERT_USER = """
                        insert into users (id, username, email, password)
                        values (?, ?, ?, ?);
                        """;

        // R (user)
        public static final String GET_USER_BY_EMAIL = """
                        select * from users where email=?;
                        """;

        public static final String COUNT_USER_BY_EMAIL = """
                        select count(*) as count from users where email=?;
                        """;

        public static final String GET_USER_BY_ID = """
                        select * from users where id=?;
                        """;

        // C (google calendar)
        public static final String INSERT_BOOKING = """
                        insert into bookings (id, userid, starttime, endtime)
                        values (?, ?, ?, ?);
                        """;

        // R (google calendar)
        public static final String GET_BOOKINGS_BY_USERID = """
                        select * from bookings where id = ""
                        """;
}
