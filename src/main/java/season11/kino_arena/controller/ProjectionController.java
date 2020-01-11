package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.AuthorizationException;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.CinemaHallDAO;
import season11.kino_arena.model.dao.MovieDAO;
import season11.kino_arena.model.dao.ProjectionDAO;
import season11.kino_arena.model.dao.TicketDAO;
import season11.kino_arena.model.dto.MessageDTO;
import season11.kino_arena.model.dto.ProjectionDTO;
import season11.kino_arena.model.dto.ProjectionTimeAndDurationDTO;
import season11.kino_arena.model.pojo.Projection;
import season11.kino_arena.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class ProjectionController {

    @Autowired
    private ProjectionDAO projectionDAO;
    @Autowired
    private MovieDAO movieDAO;
    @Autowired
    private CinemaHallDAO cinemaHallDAO;
    @Autowired
    private TicketDAO ticketDAO;

    @PostMapping("/projections/add")
    public Projection addProjection(@RequestBody ProjectionDTO reqProjection, HttpSession session) throws SQLException, NotFoundException, BadRequestException {
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        ArrayList<ProjectionTimeAndDurationDTO> allProjectionsForTheChosenHall =
                projectionDAO.getAllProjectionsForHall(reqProjection.getHall(),reqProjection.getId());
        allProjectionsForTheChosenHall.sort((p1,p2)->p1.getDateTime().compareTo(p2.getDateTime()));
        ProjectionTimeAndDurationDTO projectionToBeAdded =
                new ProjectionTimeAndDurationDTO(reqProjection.getDateTime(),movieDAO.getById(reqProjection.getMovie()).getRuntimeInMin());
        if (thereIsSpace(allProjectionsForTheChosenHall,projectionToBeAdded)){
        projectionDAO.addProjection(reqProjection);
        return new Projection(reqProjection,
                movieDAO.getById(reqProjection.getMovie()),
                cinemaHallDAO.getById(reqProjection.getHall()));
        }
        else {
            throw new BadRequestException("Projection can not be added in this time interval.");
        }
    }

    @PutMapping("/projections")
    public Projection editProjection(@RequestBody ProjectionDTO reqProjection, HttpSession session) throws SQLException, NotFoundException, BadRequestException {
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        if (reqProjection.getId()==0){
            throw new BadRequestException("Projection ID is missing.");
        }
        ArrayList<ProjectionTimeAndDurationDTO> allProjectionsForTheChosenHall =
                projectionDAO.getAllProjectionsForHall(reqProjection.getHall(),reqProjection.getId());
        allProjectionsForTheChosenHall.sort((p1,p2)->p1.getDateTime().compareTo(p2.getDateTime()));
        ProjectionTimeAndDurationDTO projectionToBeEdited =
                new ProjectionTimeAndDurationDTO(reqProjection.getDateTime(),movieDAO.getById(reqProjection.getMovie()).getRuntimeInMin());
        if (thereIsSpace(allProjectionsForTheChosenHall,projectionToBeEdited)){
        projectionDAO.editProjection(reqProjection);
        return new Projection(reqProjection,
                movieDAO.getById(reqProjection.getMovie()),
                cinemaHallDAO.getById(reqProjection.getHall()));
        }
        else {
            throw new BadRequestException("Projection can not be edited to this state, due to the time interval.");
        }
    }

    @DeleteMapping("projections/{id}")
    public MessageDTO deleteProjection(@PathVariable(name = "id") long id, HttpSession session) throws NotFoundException, SQLException {
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        ticketDAO.deleteTicketsByProjectionId(id);
        projectionDAO.deleteProjection(id);
        return new MessageDTO("Projection deleted successfully.");
    }

    @GetMapping("/projections/{cinema_id}")
    public ArrayList<Projection> getAllProjectionForCertainCinema(@PathVariable(name = "cinema_id") long cinema_id)
                                                                                throws SQLException, NotFoundException {
        return projectionDAO.getAllProjectionForCertainCinema(cinema_id);
    }

    //Validation methods for adding new projection:
    private boolean thereIsSpace(ArrayList<ProjectionTimeAndDurationDTO> projections, ProjectionTimeAndDurationDTO newProjection){
        if (projections.isEmpty()){
            return true;
        }
        for (int i = 0; i < projections.size(); i++) {
            if (projections.get(i).getDateTime().isAfter(newProjection.getDateTime())){
                if (i==0){
                    return projectionCanHappenBetween(null,projections.get(i),newProjection);
                }
                else {
                    return projectionCanHappenBetween(projections.get(i-1),projections.get(i),newProjection);
                }
            }
        }
        return projectionCanHappenBetween(projections.get(projections.size()-1),null,newProjection);
    }

    private boolean projectionCanHappenBetween(ProjectionTimeAndDurationDTO leftBorder, ProjectionTimeAndDurationDTO rightBorder, ProjectionTimeAndDurationDTO middle){
        if (leftBorder==null){
            return middle.getDateTime().plusMinutes(middle.getDuration()).isBefore(rightBorder.getDateTime());
        }
        if (rightBorder==null){
            return leftBorder.getDateTime().plusMinutes(leftBorder.getDuration()).isBefore(middle.getDateTime());
        }
        return leftBorder.getDateTime().plusMinutes(leftBorder.getDuration()).isBefore(middle.getDateTime()) &&
                middle.getDateTime().plusMinutes(middle.getDuration()).isBefore(rightBorder.getDateTime());
    }
}
