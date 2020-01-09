package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.model.dto.AddProjectionDTO;

import java.sql.*;

@Component
public class ProjectionDAO {

    private static final String ADD_PROJECTION_SQL = "INSERT INTO projections (" +
            "movie_id, " +
            "cinema_hall_id, " +
            "date_time) " +
            "VALUES (?,?,?);";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addProjection(AddProjectionDTO projection) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(ADD_PROJECTION_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, projection.getMovie());
            ps.setLong(2, projection.getHall());
            ps.setTimestamp(3, Timestamp.valueOf(projection.getDateTime()));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            projection.setId(keys.getLong(1));
        }
    }

}
