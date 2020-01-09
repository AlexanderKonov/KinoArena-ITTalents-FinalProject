package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.CinemaHallDAO;
import season11.kino_arena.model.dao.MovieDAO;
import season11.kino_arena.model.dao.ProjectionDAO;
import season11.kino_arena.model.dto.AddProjectionDTO;
import season11.kino_arena.model.pojo.CinemaHall;
import season11.kino_arena.model.pojo.Movie;
import season11.kino_arena.model.pojo.Projection;

import java.sql.SQLException;

@RestController
public class ProjectionController {

    @Autowired
    private ProjectionDAO projectionDAO;
    @Autowired
    private MovieDAO movieDAO;
    @Autowired
    private CinemaHallDAO cinemaHallDAO;

    @PostMapping("/projections/add")
    public Projection addMovie(@RequestBody AddProjectionDTO reqProjection) throws SQLException, NotFoundException {
        projectionDAO.addProjection(reqProjection);
        return new Projection(reqProjection,
                movieDAO.getById(reqProjection.getMovie()),
                cinemaHallDAO.getById(reqProjection.getHall()));
    }

}
