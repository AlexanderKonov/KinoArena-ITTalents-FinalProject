package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.CinemaDAO;
import season11.kino_arena.model.dao.CinemaHallDAO;
import season11.kino_arena.model.dao.CinemaHallTypeDAO;
import season11.kino_arena.model.dao.TicketDAO;
import season11.kino_arena.model.dto.CinemaHallDTO;
import season11.kino_arena.model.dto.MessageDTO;
import season11.kino_arena.model.pojo.Cinema;
import season11.kino_arena.model.pojo.CinemaHall;
import season11.kino_arena.model.pojo.CinemaHallType;

import java.sql.SQLException;

@RestController
public class CinemaHallController {

    @Autowired
    CinemaHallDAO cinemaHallDAO;
    @Autowired
    CinemaHallTypeDAO cinemaHallTypeDAO;
    @Autowired
    CinemaDAO cinemaDAO;
    @Autowired
    TicketDAO ticketDAO;

    @PostMapping("/halls/add")
    public CinemaHall addCinemaHall(@RequestBody CinemaHallDTO cinemaHallWithIndexes) throws SQLException, NotFoundException {
        cinemaHallDAO.addCinemaHall(cinemaHallWithIndexes);
        CinemaHallType cinemaHallType = cinemaHallTypeDAO.getCinemaHallTypeById(cinemaHallWithIndexes.getCinemaHallTypeId());
        Cinema cinema = cinemaDAO.getCinemaById(cinemaHallWithIndexes.getCinemaId());
        return new CinemaHall(cinemaHallWithIndexes,cinemaHallType,cinema);
    }

    @PutMapping("/halls")
    public CinemaHall editCinemaHall(@RequestBody CinemaHallDTO updatedCinemaHall) throws SQLException, NotFoundException, BadRequestException {
        CinemaHall oldHall = cinemaHallDAO.getById(updatedCinemaHall.getId());
        cinemaHallDAO.updateCinemaHall(updatedCinemaHall);
        if (updatedCinemaHall.getNumberOfRows()<oldHall.getNumberOfRows()||updatedCinemaHall.getNumberOfSeatsPerRow()<oldHall.getNumberOfSeatsPerRow()){
            ticketDAO.deleteTicketsAfterHallResize(updatedCinemaHall.getNumberOfRows(),updatedCinemaHall.getNumberOfSeatsPerRow(),updatedCinemaHall.getId());
        }
        CinemaHallType cinemaHallType = cinemaHallTypeDAO.getCinemaHallTypeById(updatedCinemaHall.getCinemaHallTypeId());
        Cinema cinema = cinemaDAO.getCinemaById(updatedCinemaHall.getCinemaId());
        return new CinemaHall(updatedCinemaHall,cinemaHallType,cinema);
    }

    @DeleteMapping("/halls/{id}")
    public MessageDTO deleteCinemaHall(@PathVariable (name = "id") long id) throws NotFoundException, SQLException {
        cinemaHallDAO.deleteCinemaHall(id);
        return new MessageDTO("Cinema hall deleted successfully.");
    }
}
