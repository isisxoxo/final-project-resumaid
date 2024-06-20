package nus.iss.edu.sg.final_project_backend_resumaid.exceptions;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException() {
        super();
    }

    public EmailExistsException(String msg) {
        super(msg);
    }
}
