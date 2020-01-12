package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.AuthorizationException;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.model.dao.CinemaDAO;
import season11.kino_arena.model.dao.CinemaHallDAO;
import season11.kino_arena.model.dao.CinemaHallTypeDAO;
import season11.kino_arena.model.dao.TicketDAO;
import season11.kino_arena.model.dto.CinemaHallDTO;
import season11.kino_arena.model.dto.MessageDTO;
import season11.kino_arena.model.pojo.Cinema;
import season11.kino_arena.model.pojo.CinemaHall;
import season11.kino_arena.model.pojo.CinemaHallType;
import season11.kino_arena.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class CinemaHallController {

     static final int MIN_ROWS_AND_SEATS_IN_HALL = 1;
     static final int MAX_ROWS_AND_SEATS_IN_HALL = 200;

    @Autowired
    CinemaHallDAO cinemaHallDAO;
    @Autowired
    CinemaHallTypeDAO cinemaHallTypeDAO;
    @Autowired
    CinemaDAO cinemaDAO;
    @Autowired
    TicketDAO ticketDAO;

    @PostMapping("/halls/add")
    public CinemaHall addCinemaHall(@RequestBody CinemaHallDTO cinemaHallWithIndexes, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        validateCinemaHall(cinemaHallWithIndexes);
        cinemaHallDAO.addCinemaHall(cinemaHallWithIndexes);
        CinemaHallType cinemaHallType = cinemaHallTypeDAO.getCinemaHallTypeById(cinemaHallWithIndexes.getCinemaHallTypeId());
        Cinema cinema = cinemaDAO.getCinemaById(cinemaHallWithIndexes.getCinemaId());
        return new CinemaHall(cinemaHallWithIndexes,cinemaHallType,cinema);
    }

    @PutMapping("/halls")
    public CinemaHall editCinemaHall(@RequestBody CinemaHallDTO updatedCinemaHall, HttpSession session) throws SQLException{
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        validateCinemaHall(updatedCinemaHall);
        CinemaHall oldHall = cinemaHallDAO.getById(updatedCinemaHall.getId());
        if (updatedCinemaHall.getCinemaId()!=0){
            throw new BadRequestException("You are not allowed to set the cinema ID.");
        }
        cinemaHallDAO.updateCinemaHall(updatedCinemaHall);
        if (updatedCinemaHall.getNumberOfRows()<oldHall.getNumberOfRows()||updatedCinemaHall.getNumberOfSeatsPerRow()<oldHall.getNumberOfSeatsPerRow()){
            ticketDAO.deleteTicketsAfterHallResize(updatedCinemaHall.getNumberOfRows(),updatedCinemaHall.getNumberOfSeatsPerRow(),updatedCinemaHall.getId());
        }
        CinemaHallType cinemaHallType = cinemaHallTypeDAO.getCinemaHallTypeById(updatedCinemaHall.getCinemaHallTypeId());
        Cinema cinema = oldHall.getCinema();
        return new CinemaHall(updatedCinemaHall,cinemaHallType,cinema);
    }

    @DeleteMapping("/halls/{id}")
    public MessageDTO deleteCinemaHall(@PathVariable (name = "id") long id, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        cinemaHallDAO.deleteCinemaHall(id);
        return new MessageDTO("Cinema hall deleted successfully.");
    }

    private void validateCinemaHall(CinemaHallDTO hallDTO) throws BadRequestException {
        if (hallDTO.getNumberOfRows()< MIN_ROWS_AND_SEATS_IN_HALL ||
                hallDTO.getNumberOfRows() > MAX_ROWS_AND_SEATS_IN_HALL){
            throw new BadRequestException("Number of rows is not valid.");
        }
        if (hallDTO.getNumberOfSeatsPerRow()< MIN_ROWS_AND_SEATS_IN_HALL ||
                hallDTO.getNumberOfSeatsPerRow() > MAX_ROWS_AND_SEATS_IN_HALL){
            throw new BadRequestException("Number of seats per row is not valid.");
        }
    }
}
