package az.edu.turing.usermanager.exception;

import az.edu.turing.usermanager.model.constant.ErrorCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleNotFoundException(NotFoundException e) {
        logError(e);
        return ResponseEntity.status(NOT_FOUND)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.NOT_FOUND)
                        .errorMessage(e.getMessage())
                        .requestId(UUID.randomUUID())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<GlobalErrorResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        logError(e);
        return ResponseEntity.status(BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.ALREADY_EXIST)
                        .errorMessage(e.getMessage())
                        .requestId(UUID.randomUUID())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<GlobalErrorResponse> handleInvalidInputException(InvalidInputException e) {
        logError(e);
        return ResponseEntity.status(BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.INVALID_INPUT)
                        .errorMessage(e.getMessage())
                        .requestId(UUID.randomUUID())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorResponse> handleMethodArgumentNotValidException
            (MethodArgumentNotValidException e) {
        logError(e);
        String validationErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(BAD_REQUEST)
                .body(GlobalErrorResponse.builder()
                        .errorCode(ErrorCode.INVALID_INPUT)
                        .errorMessage("Validation failed: " + validationErrors)
                        .requestId(UUID.randomUUID())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    private static void logError(Exception e) {
        log.error("{} happened: {}", e.getClass().getSimpleName(), e.getMessage());
    }
}
