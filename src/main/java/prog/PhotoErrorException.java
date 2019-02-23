package prog;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhotoErrorException extends RuntimeException {
    public PhotoErrorException(String message) {
        super(message);
    }
}