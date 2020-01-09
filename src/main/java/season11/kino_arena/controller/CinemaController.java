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

    @PostMapping("/cinemas/edit")
    public Cinema editCinema(@RequestBody Cinema cinema) throws SQLException, NotFoundException {
        cinemaDAO.updateCinema(cinema);
        cinema = cinemaDAO.getCinemaById(cinema.getId());
        return cinema;
    }

    @PostMapping("/cinemas/delete/{id}")
    public String deleteCinema(@PathVariable(name = "id") long id) throws NotFoundException, SQLException {
        cinemaDAO.deleteCinema(id);
        //TODO change the plain text to something better
        return "Cinema deleted successfully!";
    }
}
