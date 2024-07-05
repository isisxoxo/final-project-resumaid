package nus.iss.edu.sg.final_project_backend_resumaid.exceptions;

public class UserExistsException extends RuntimeException {

    public UserExistsException() {
        super();
    }

    public UserExistsException(String msg) {
        super(msg);
    }
}
