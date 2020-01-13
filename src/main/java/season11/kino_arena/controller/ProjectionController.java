package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.AuthorizationException;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.*;
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
    @Autowired
    private CinemaDAO cinemaDAO;
    @Autowired
    private VideoFormatDAO videoFormatDAO;
    @PostMapping("/projections/add")
    public Projection addProjection(@RequestBody ProjectionDTO reqProjection, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        validateProjectionDto(reqProjection);
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

    private void validateProjectionDto(ProjectionDTO reqProjection) throws SQLException {
        if (movieDAO.getById(reqProjection.getMovie()) == null){
            throw new BadRequestException("Movie doesn't exist!");
        }
        if (cinemaHallDAO.getById(reqProjection.getHall()) == null){
            throw new BadRequestException("Cinema hall doesn't exist.");
        }
        if (reqProjection.getDateTime()==null){
            throw new BadRequestException("DateTime is incorrect.");
        }
    }

    @PutMapping("/projections")
    public Projection editProjection(@RequestBody ProjectionDTO reqProjection, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        if (reqProjection.getId()==0){
            throw new BadRequestException("Projection ID is missing.");
        }
        validateProjectionDto(reqProjection);
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
    public MessageDTO deleteProjection(@PathVariable(name = "id") long id, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        //ticketDAO.deleteTicketsByProjectionId(id);
        projectionDAO.deleteProjection(id);
        return new MessageDTO("Projection deleted successfully.");
    }

    @GetMapping("/projections/{cinemaId}")
    public ArrayList<Projection> getAllProjectionForCertainCinema(@PathVariable(name = "cinemaId") long cinemaId) throws SQLException {
        cinemaDAO.getCinemaById(cinemaId);
        return projectionDAO.getAllProjectionForCertainCinema(cinemaId);
    }

    @GetMapping("projections/{cinemaId}/{projectionTypeId}")
    public ArrayList<Projection> getAllProjectionsByCinemaAndProjectionType(
                                                        @PathVariable(name = "cinemaId") long cinemaId,
                                                        @PathVariable(name = "projectionTypeId") long projectionTypeId)
                                                                                                    throws SQLException {
        cinemaDAO.getCinemaById(cinemaId);
        if (videoFormatDAO.getById(projectionTypeId) == null) {
            throw new NotFoundException("Projection type not found.");
        }
        ArrayList<Projection> allProjectionsForCinema = projectionDAO.getAllProjectionForCertainCinema(cinemaId);
        ArrayList<Projection> result = new ArrayList<>();
        for (Projection p :
                allProjectionsForCinema) {
            if (p.getMovie().getVideoFormat().getId() == projectionTypeId){
                result.add(p);
            }
        }
        return result;
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
