package nus.iss.edu.sg.final_project_backend_resumaid.utils;

public interface Queries {

        // C
        public static final String INSERT_USER = """
                        insert into users (id, username, email, password)
                        values (?, ?, ?, ?);
                        """;

        // R
        public static final String GET_USER_BY_EMAIL = """
                        select * from users where email=?;
                        """;

        public static final String COUNT_USER_BY_EMAIL = """
                        select count(*) as count from users where email=?;
                        """;

}
