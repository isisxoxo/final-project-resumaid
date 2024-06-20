package nus.iss.edu.sg.final_project_backend_resumaid.exceptions;

public class LoginException extends RuntimeException {

    public LoginException() {
        super();
    }

    public LoginException(String msg) {
        super(msg);
    }
}
