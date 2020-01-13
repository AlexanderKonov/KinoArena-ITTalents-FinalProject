package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.pojo.Cinema;
import season11.kino_arena.model.pojo.CinemaHallType;

import java.sql.*;

@Component
public class CinemaHallTypeDAO {

    private static final String GET_CINEMA_HALL_TYPE_BY_ID = "SELECT * FROM cinema_hall_types WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CinemaHallType getCinemaHallTypeById(long id) throws SQLException, NotFoundException {
        try (
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(GET_CINEMA_HALL_TYPE_BY_ID, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CinemaHallType(rs.getLong("id"),
                        rs.getString("name"));
            } else {
                return null;
            }
        }
    }
}
