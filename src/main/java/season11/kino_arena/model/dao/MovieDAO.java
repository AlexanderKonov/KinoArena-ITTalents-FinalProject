package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.model.dto.AddMovieDTO;

import java.sql.*;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addMovie(AddMovieDTO movie) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(ADD_MOVIE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, movie.getName());
            ps.setString(2, movie.getDescription());
            ps.setInt(3, movie.getRuntimeInMin());
            ps.setDate(4, movie.getPremiere());
            ps.setLong(5, movie.getGenre());
            ps.setLong(6, movie.getRestriction());
            ps.setDouble(7, movie.getRating());
            ps.setBoolean(8, movie.isDubbed());
            ps.setLong(9, movie.getVideoFormat());
            ps.setString(10, movie.getCast());
            ps.setString(11, movie.getDirector());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            movie.setId(keys.getLong(1));
        }
    }

    public void deleteMovie(long id) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(DELETE_MOVIE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            if(ps.executeUpdate() == 0){
//                throw new NotFoundException("Movie was not found.")
            }
        }
    }

    public void editMovie(AddMovieDTO movie) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(EDIT_MOVIE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, movie.getName());
            ps.setString(2, movie.getDescription());
            ps.setInt(3, movie.getRuntimeInMin());
            ps.setDate(4, movie.getPremiere());
            ps.setLong(5, movie.getGenre());
            ps.setLong(6, movie.getRestriction());
            ps.setDouble(7, movie.getRating());
            ps.setBoolean(8, movie.isDubbed());
            ps.setLong(9, movie.getVideoFormat());
            ps.setString(10, movie.getCast());
            ps.setString(11, movie.getDirector());
            ps.setLong(12, movie.getId());
            ps.executeUpdate();
        }
    }
}