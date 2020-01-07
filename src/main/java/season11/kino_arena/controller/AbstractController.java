package season11.kino_arena.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.model.dto.ErrorDTO;

import java.sql.SQLException;
import java.time.LocalDateTime;

public abstract class AbstractController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleBadRequestException(Exception e){
        return new ErrorDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleSQLExceptions(Exception e){
        return new ErrorDTO(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getClass().getName());
    }
}
