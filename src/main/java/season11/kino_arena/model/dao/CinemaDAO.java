package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.pojo.Cinema;

import java.sql.*;
import java.time.LocalDateTime;

@Component
public class CinemaDAO {

    private static final String ADD_CINEMA_SQL = "INSERT INTO cinemas (" +
            "name," +
            "address," +
            "telephone_number," +
            "cinema_info," +
            "city" +
            ")" +
            "VALUES (?,?,?,?,?);";

    private static final String DELETE_CINEMA_SQL = "DELETE FROM cinemas WHERE id= ?;";
    private static final String EDIT_CINEMA_SQL =
            "UPDATE cinemas " +
                    "SET " +
                    "name = ?, " +
                    "address = ?, " +
                    "telephone_number = ?, " +
                    "cinema_info = ?, " +
                    "city = ? " +
                    "WHERE " +
                    "id=?";
    private static final String GET_CINEMA_BY_ID = "SELECT * FROM cinemas WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addCinema(Cinema cinema) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement ps = connection.prepareStatement(ADD_CINEMA_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cinema.getName());
            ps.setString(2, cinema.getAddress());
            ps.setString(3, cinema.getTelephoneNumber());
            ps.setString(4, cinema.getCinemaInfo());
            ps.setString(5,cinema.getCity());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            cinema.setId(keys.getLong(1));
        }
    }

    public void deleteCinema(long cinemaID) throws SQLException, NotFoundException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement ps = connection.prepareStatement(DELETE_CINEMA_SQL)){
            ps.setLong(1,cinemaID);
            if (ps.executeUpdate()==0) {
                throw new NotFoundException("Cinema was not found.");
            }
        }
    }

    public void updateCinema(Cinema cinema) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement ps = connection.prepareStatement(EDIT_CINEMA_SQL)){
            ps.setString(1,cinema.getName());
            ps.setString(2,cinema.getAddress());
            ps.setString(3,cinema.getTelephoneNumber());
            ps.setString(4,cinema.getCinemaInfo());
            ps.setString(5,cinema.getCity());
            ps.setLong(6,cinema.getId());
            ps.executeUpdate();
        }
    }

    public Cinema getCinemaById(long id) throws SQLException, NotFoundException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement ps = connection.prepareStatement(GET_CINEMA_BY_ID, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Cinema(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("telephone_number"),
                        rs.getString("cinema_info"),
                        rs.getString("city"));
            }
            else{
                throw new NotFoundException("Cinema not found");
            }
    }
}
}
