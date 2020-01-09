package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.model.dao.GenreDAO;
import season11.kino_arena.model.dao.MovieDAO;
import season11.kino_arena.model.dao.RestrictionDAO;
import season11.kino_arena.model.dao.VideoFormatDAO;
import season11.kino_arena.model.dto.AddMovieDTO;
import season11.kino_arena.model.pojo.Movie;

import java.sql.SQLException;

@RestController
public class MovieController {

    @Autowired
    private MovieDAO movieDAO;
    @Autowired
    private GenreDAO genreDAO;
    @Autowired
    private RestrictionDAO restrictionDAO;
    @Autowired
    private VideoFormatDAO videoFormatDAO;

    @PostMapping("/movies/add")
    public Movie addMovie(@RequestBody AddMovieDTO reqMovie) throws SQLException {
        movieDAO.addMovie(reqMovie);
        return new Movie(reqMovie,
                genreDAO.getById(reqMovie.getGenre()),
                restrictionDAO.getById(reqMovie.getRestriction()),
                videoFormatDAO.getById(reqMovie.getVideoFormat()));
    }

    @DeleteMapping("/movies/{id}")
    public String deleteCinema(@PathVariable(name = "id") long id) throws SQLException {
        movieDAO.deleteMovie(id);
        //TODO change the plain text to something better
        return "Movie deleted successfully!";
    }

    @PutMapping("/movies")
    public Movie editMovie(@RequestBody AddMovieDTO reqMovie) throws SQLException, BadRequestException {
        movieDAO.editMovie(reqMovie);
        return new Movie(reqMovie,
                genreDAO.getById(reqMovie.getGenre()),
                restrictionDAO.getById(reqMovie.getRestriction()),
                videoFormatDAO.getById(reqMovie.getVideoFormat()));
    }
}
