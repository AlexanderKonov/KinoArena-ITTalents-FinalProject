package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.AuthorizationException;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.CinemaDAO;
import season11.kino_arena.model.dto.MessageDTO;
import season11.kino_arena.model.pojo.Cinema;
import season11.kino_arena.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class CinemaController {

    @Autowired
    CinemaDAO cinemaDAO;

    @PostMapping("/cinemas/add")
    public Cinema addCinema(@RequestBody Cinema cinema, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        validateCinemaData(cinema);//throws exception
        cinemaDAO.addCinema(cinema);
        return cinema;
    }

    @PutMapping("cinemas")
    public Cinema editCinema(@RequestBody Cinema cinema, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        if (cinemaDAO.getCinemaById(cinema.getId())==null){
            throw new NotFoundException("Cinema was not found.");
        }
        validateCinemaData(cinema);//throws exception
        cinemaDAO.updateCinema(cinema);
        return cinema;
    }

    @DeleteMapping("cinemas/{id}")
    public MessageDTO deleteCinema(@PathVariable(name = "id") long id, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        if (cinemaDAO.getCinemaById(id)==null){
            throw new NotFoundException("Cinema was not found.");
        }
        cinemaDAO.deleteCinema(id);
        return new MessageDTO("Cinema deleted successfully.");
    }

    @GetMapping("/cinemas")
    public ArrayList<Cinema> getAllCinemas() throws SQLException {
        return cinemaDAO.getAllCinemas();
    }

    @GetMapping("/cinemas/{city}")
    public ArrayList<Cinema> getAllCinemasByCity(@PathVariable(name = "city") String city) throws SQLException {
        return cinemaDAO.getAllCinemasByCity(city);
    }

    private void validateCinemaData(Cinema cinema) throws BadRequestException {
        if (!isValidPhone(cinema.getTelephoneNumber())){
            throw new BadRequestException("Telephone number is not valid.");
        }
        if (!isValidCity(cinema.getCity())){
            throw new BadRequestException("City is not valid.");
        }
        if (!isValidAddress(cinema.getAddress())){
            throw new BadRequestException("Address is not valid.");
        }
        if (cinema.getName().length() < 2 || cinema.getName().length() > 15 ||
            cinema.getAddress().length() < 2 || cinema.getAddress().length() > 45 ||
            cinema.getCity().length() < 2 || cinema.getCity().length() > 15 ||
            cinema.getCinemaInfo().length() < 2 || cinema.getCinemaInfo().length() > 15){
            throw new BadRequestException("Check your data");
        }
    }

    private boolean isValidCity(String city){
        return city.matches( "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" );
    }

    private boolean isValidPhone(String phone){
        return phone.matches( "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");
    }

    private boolean isValidAddress( String address )
    {
        return address.matches("^[#.0-9a-zA-Z\\s,-]+$" );
    }
}
