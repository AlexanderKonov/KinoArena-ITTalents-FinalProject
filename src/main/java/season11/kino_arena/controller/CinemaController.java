package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.CinemaDAO;
import season11.kino_arena.model.dto.MessageDTO;
import season11.kino_arena.model.pojo.Cinema;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class CinemaController {

    @Autowired
    CinemaDAO cinemaDAO;

    @PostMapping("/cinemas/add")
    public Cinema addCinema(@RequestBody Cinema cinema) throws SQLException {
        cinemaDAO.addCinema(cinema);
        return cinema;
    }

    @PutMapping("cinemas")
    public Cinema editCinema(@RequestBody Cinema cinema) throws SQLException, NotFoundException, BadRequestException {
        cinemaDAO.updateCinema(cinema);
        return cinema;
    }

    @DeleteMapping("cinemas/{id}")
    public MessageDTO deleteCinema(@PathVariable(name = "id") long id) throws NotFoundException, SQLException {
        cinemaDAO.deleteCinema(id);
        return new MessageDTO("Cinema deleted successfully.");
    }

    @GetMapping("/cinemas")
    public ArrayList<Cinema> getAllCinemas() throws SQLException {
        return cinemaDAO.getAllCinemas();
    }

    @GetMapping("/cinemas/{city}")
    public ArrayList<Cinema> getAllCinemasByCity(@PathVariable(name = "city") String city) throws SQLException, BadRequestException {
        ArrayList<Cinema> cinemas = cinemaDAO.getAllCinemasByCity(city);
        return cinemas;
    }

}
