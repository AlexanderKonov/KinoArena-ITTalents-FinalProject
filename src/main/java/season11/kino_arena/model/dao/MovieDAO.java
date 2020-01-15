package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dto.MovieDTO;
import season11.kino_arena.model.pojo.Movie;

import java.sql.*;
import java.util.ArrayList;

@Component
public class MovieDAO {

    private static final String ADD_MOVIE_SQL = "INSERT INTO movies (" +
                                                "`name`, " +
                                                "description, " +
                                                "runtime_in_min, " +
                                                "premiere, " +
                                                "genre_id, " +
                                                "restriction_id, " +
                                                "rating, " +
                                                "is_dubbed, " +
                                                "video_fomat_id, " +
                                                "`cast`, " +
                                                "directors) " +
                                                "VALUES (?,?,?,?,?,?,?,?,?,?,?);";

    private static final String DELETE_MOVIE_SQL = "DELETE FROM movies WHERE id = ?;";

    private static final String EDIT_MOVIE_SQL = "UPDATE movies\n" +
                                                "SET " +
                                                "name = ?," +
                                                "description = ?," +
                                                "runtime_in_min = ?," +
                                                "premiere = ?," +
                                                "genre_id = ?," +
                                                "restriction_id = ?," +
                                                "rating = ?," +
                                                "is_dubbed = ?," +
                                                "video_fomat_id = ?," +
                                                "`cast` = ?," +
                                                "directors = ?" +
                                                "WHERE id = ?";

    private static final String SELECT_BY_ID = "SELECT * FROM movies WHERE id = ?";

    private static final String SELECT_BY_MULTIPLE_FIELDS = "SELECT * FROM movies WHERE name= ? AND premiere = ? ";
    private static final String SELECT_ALL_MOVIES = "SELECT * FROM movies ";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GenreDAO genreDAO;
    @Autowired
    private RestrictionDAO restrictionDAO;
    @Autowired
    private VideoFormatDAO videoFormatDAO;
    @Autowired
    private ProjectionDAO projectionDAO;

    public void addMovie(MovieDTO movie) throws SQLException {
        try (
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(ADD_MOVIE_SQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, movie.getName());
                ps.setString(2, movie.getDescription());
                ps.setInt(3, movie.getRuntimeInMin());
                ps.setDate(4, movie.getPremiere());
                ps.setLong(5, movie.getGenre());
                ps.setLong(6, movie.getRestriction());
                ps.setDouble(7, movie.getRating());
                ps.setBoolean(8, movie.getIsDubbed());
                ps.setLong(9, movie.getVideoFormat());
                ps.setString(10, movie.getCast());
                ps.setString(11, movie.getDirector());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                movie.setId(keys.getLong(1));
        }
    }

    public void deleteMovie(long id) throws SQLException, NotFoundException {
        try (
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_MOVIE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public void editMovie(MovieDTO movie) throws SQLException, BadRequestException {
        try (
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(EDIT_MOVIE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, movie.getName());
            ps.setString(2, movie.getDescription());
            ps.setInt(3, movie.getRuntimeInMin());
            ps.setDate(4, movie.getPremiere());
            ps.setLong(5, movie.getGenre());
            ps.setLong(6, movie.getRestriction());
            ps.setDouble(7, movie.getRating());
            ps.setBoolean(8, movie.getIsDubbed());
            ps.setLong(9, movie.getVideoFormat());
            ps.setString(10, movie.getCast());
            ps.setString(11, movie.getDirector());
            ps.setLong(12, movie.getId());
            ps.executeUpdate();
        }
    }

    public boolean movieExists(MovieDTO movie) throws SQLException {
        try (
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_BY_MULTIPLE_FIELDS)) {
            ps.setString(1, movie.getName());
            ps.setDate(2, movie.getPremiere());
            return ps.executeQuery().next();
        }
    }

    public Movie getById(long id) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                MovieDTO movieDTO = new MovieDTO(rs.getLong("id"),
                                                        rs.getString("name"),
                                                        rs.getString("description"),
                                                        rs.getInt("runtime_in_min"),
                                                        rs.getDate("premiere"),
                                                        rs.getLong("genre_id"),
                                                        rs.getLong("restriction_id"),
                                                        rs.getDouble("rating"),
                                                        rs.getBoolean("is_dubbed"),
                                                        rs.getLong("video_fomat_id"),
                                                        rs.getString("cast"),
                                                        rs.getString("directors"));
                return new Movie(movieDTO,
                        genreDAO.getById(movieDTO.getGenre()),
                        restrictionDAO.getById(movieDTO.getRestriction()),
                        videoFormatDAO.getById(movieDTO.getVideoFormat()));
            }
            else{
                return null;
            }
        }
    }

    public ArrayList<Movie> getAllMovies() throws SQLException {
        ArrayList<MovieDTO> allMoviesDto = new ArrayList<>();
        ArrayList<Movie> allMovies = new ArrayList<>();
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MOVIES)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                allMoviesDto.add(new MovieDTO(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("runtime_in_min"),
                        rs.getDate("premiere"),
                        rs.getLong("genre_id"),
                        rs.getLong("restriction_id"),
                        rs.getDouble("rating"),
                        rs.getBoolean("is_dubbed"),
                        rs.getLong("video_fomat_id"),
                        rs.getString("cast"),
                        rs.getString("directors")));
            }
        }
        for (MovieDTO movieDTO :
                allMoviesDto) {
            allMovies.add(new Movie(movieDTO,
                    genreDAO.getById(movieDTO.getGenre()),
                    restrictionDAO.getById(movieDTO.getRestriction()),
                    videoFormatDAO.getById(movieDTO.getVideoFormat())));
        }
        return allMovies;
    }
}