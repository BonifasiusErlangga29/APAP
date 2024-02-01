package apap.tk.rumahsehat.exceptions;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String errorMessage) {
        super(errorMessage);
    }
}
