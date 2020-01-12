package season11.kino_arena.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import season11.kino_arena.exceptions.AuthorizationException;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dto.ErrorDTO;

import java.sql.SQLException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<Object> handleAuthorizationException(RuntimeException e) {
        return new ResponseEntity<>(new ErrorDTO(
                                                e.getMessage(),
                                                HttpStatus.UNAUTHORIZED.value(),
                                                LocalDateTime.now(),
                                                e.getClass().getName()),
                                    HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleBadRequestException(RuntimeException e) {
        return new ResponseEntity<>(new ErrorDTO(
                                                    e.getMessage(),
                                                    HttpStatus.BAD_REQUEST.value(),
                                                    LocalDateTime.now(),
                                                    e.getClass().getName()),
                                     HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleSQLException(RuntimeException e) {
        return new ResponseEntity<>(new ErrorDTO(
                                                    e.getMessage(),
                                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                    LocalDateTime.now(),
                                                    e.getClass().getName()),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException e) {
        return new ResponseEntity<>(new ErrorDTO(
                                                e.getMessage(),
                                                HttpStatus.NOT_FOUND.value(),
                                                LocalDateTime.now(),
                                                e.getClass().getName()),
                                     HttpStatus.NOT_FOUND);
    }

}
