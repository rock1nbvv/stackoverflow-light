package rockinbvv.stackoverflowlight.app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rockinbvv.stackoverflowlight.app.ResponseWrapper;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleAppException(ApplicationException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Error-Code", ex.getErrorCode());

        return ResponseEntity
                .status(ex.getHttpStatus())
                .headers(headers)
                .body(ResponseWrapper.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<Void>> handleUnhandled(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseWrapper.error("Unexpected error"));
    }
}
