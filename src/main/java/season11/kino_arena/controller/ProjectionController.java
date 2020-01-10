package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.CinemaHallDAO;
import season11.kino_arena.model.dao.MovieDAO;
import season11.kino_arena.model.dao.ProjectionDAO;
import season11.kino_arena.model.dto.ProjectionDTO;
import season11.kino_arena.model.dto.ProjectionTimeAndDurationDTO;
import season11.kino_arena.model.pojo.Projection;

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

    @PostMapping("/projections/add")
    public Projection addProjection(@RequestBody ProjectionDTO reqProjection) throws SQLException, NotFoundException, BadRequestException {
        ArrayList<ProjectionTimeAndDurationDTO> allProjectionsForTheChosenHall = projectionDAO.getAllProjectionsForHall(reqProjection.getHall());
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
    public Projection editProjection(@RequestBody ProjectionDTO reqProjection) throws SQLException, NotFoundException, BadRequestException {
        projectionDAO.editProjection(reqProjection);
        return new Projection(reqProjection,
                movieDAO.getById(reqProjection.getMovie()),
                cinemaHallDAO.getById(reqProjection.getHall()));
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