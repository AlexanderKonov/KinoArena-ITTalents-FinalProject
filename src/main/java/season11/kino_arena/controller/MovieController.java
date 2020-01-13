package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.AuthorizationException;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.GenreDAO;
import season11.kino_arena.model.dao.MovieDAO;
import season11.kino_arena.model.dao.RestrictionDAO;
import season11.kino_arena.model.dao.VideoFormatDAO;
import season11.kino_arena.model.dto.MessageDTO;
import season11.kino_arena.model.dto.MovieDTO;
import season11.kino_arena.model.pojo.*;

import javax.servlet.http.HttpSession;
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
    public Movie addMovie(@RequestBody MovieDTO reqMovie, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        validateMovieData(reqMovie);
        if (movieDAO.movieExists(reqMovie)){
            throw new BadRequestException("Movie already exists.");
        }
        Genre genre = genreDAO.getById(reqMovie.getGenre());
        if(genre == null){
            throw new BadRequestException("There is no genre like this.");
        }
        Restriction restriction = restrictionDAO.getById(reqMovie.getRestriction());
        if(restriction == null){
            throw new BadRequestException("There is no restriction like this.");
        }
        VideoFormat videoFormat = videoFormatDAO.getById(reqMovie.getVideoFormat());
        if (videoFormat==null){
            throw new NotFoundException("Video format was not found.");
        }
        movieDAO.addMovie(reqMovie);
        return new Movie(reqMovie, genre, restriction, videoFormat);
    }

    @DeleteMapping("/movies/{id}")
    public MessageDTO deleteMovie(@PathVariable(name = "id") long id, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        if (movieDAO.getById(id)==null){
            throw new NotFoundException("Movie was not found.");
        }
        movieDAO.deleteMovie(id);
        return new MessageDTO("Movie deleted successfully!");
    }

    @PutMapping("/movies")
    public Movie editMovie(@RequestBody MovieDTO reqMovie, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null || !user.getIsAdmin()){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        validateMovieData(reqMovie);
        if (movieDAO.getById(reqMovie.getId())==null){
            throw new NotFoundException("Movie was not found.");
        }
        Genre genre = genreDAO.getById(reqMovie.getGenre());
        if(genre == null){
            throw new BadRequestException("There is no genre like this.");
        }
        Restriction restriction = restrictionDAO.getById(reqMovie.getRestriction());
        if(restriction == null){
            throw new BadRequestException("There is no restriction like this.");
        }
        VideoFormat videoFormat = videoFormatDAO.getById(reqMovie.getVideoFormat());
        if (videoFormat==null){
            throw new NotFoundException("Video format was not found.");
        }
        movieDAO.editMovie(reqMovie);
        return new Movie(reqMovie, genre, restriction, videoFormat);
    }

    @GetMapping("/movies/{id}")
    public Movie getMovieById(@PathVariable(name = "id") long movieId) throws SQLException {
        Movie movie = movieDAO.getById(movieId);
        if (movie == null){
            throw new NotFoundException("This movie doesn`t exist");
        }
        return movie;
    }

    //TODO show all movies

    private void validateMovieData(MovieDTO movie) {
        if (!runtimeIsValid(movie.getRuntimeInMin())){
            throw new BadRequestException("Movie is too short or too long.");
        }
        if (movie.getRating()<0||movie.getRating()>10){
            throw new BadRequestException("Rating value is not correct.");
        }
        if (!nameIsValid(movie.getCast())){
            throw new BadRequestException("Cast value is not correct.");
        }
        if (!nameIsValid(movie.getDirector())){
            throw new BadRequestException("Director value is not correct.");
        }
    }

    private boolean runtimeIsValid(int runtime){
        return runtime>0 && runtime < 300;
    }

    private boolean nameIsValid(String name){
        return name.matches("^[\\p{L} ,.'-]+$");
    }
}
