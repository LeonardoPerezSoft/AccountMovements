package co.com.sofka.api.config.helpers;


import co.com.sofka.r2dbc.helper.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public Mono<ResponseEntity<String>> handleCustomException(CustomException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String body = "Error: " + ex.getMessage();
        return Mono.just(ResponseEntity.status(status).body(body))
                .onErrorResume(e -> Mono.error(new CustomException(e.getMessage())));
    }
}