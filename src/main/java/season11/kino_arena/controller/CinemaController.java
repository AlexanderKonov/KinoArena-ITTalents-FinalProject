package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.CinemaDAO;
import season11.kino_arena.model.pojo.Cinema;

import java.sql.SQLException;

@RestController
public class CinemaController {

    @Autowired
    CinemaDAO cinemaDAO;

    @PostMapping("/cinemas/add")
    public Cinema addCinema(@RequestBody Cinema cinema) throws SQLException {
        cinemaDAO.addCinema(cinema);
        return cinema;
    }

    @PutMapping("cinemas/")
    public Cinema editCinema(@RequestBody Cinema cinema) throws SQLException, NotFoundException, BadRequestException {
        cinemaDAO.updateCinema(cinema);
        return cinema;
    }

    @DeleteMapping("cinemas/{id}")
    public String deleteCinema(@PathVariable(name = "id") long id) throws NotFoundException, SQLException {
        cinemaDAO.deleteCinema(id);
        //TODO change the plain text to something better
        return "Cinema deleted successfully!";
    }
}
