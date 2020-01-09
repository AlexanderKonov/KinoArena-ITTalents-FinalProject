package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.model.dto.AddProjectionDTO;
import season11.kino_arena.model.dto.ProjectionTimeAndDurationDTO;
import season11.kino_arena.model.pojo.Projection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class ProjectionDAO {

    private static final String ADD_PROJECTION_SQL = "INSERT INTO projections (" +
            "movie_id, " +
            "cinema_hall_id, " +
            "date_time) " +
            "VALUES (?,?,?);";
    private static final String GET_ALL_PROJECTIONS_FOR_HALL = "SELECT date_time, runtime_in_min FROM projections " +
            "JOIN movies " +
            "WHERE cinema_hall_id = ?";

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

    public ArrayList<ProjectionTimeAndDurationDTO> getAllProjectionsForHall(long hallId) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ArrayList<ProjectionTimeAndDurationDTO> allProjectionsForHall = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(GET_ALL_PROJECTIONS_FOR_HALL)){
            ps.setLong(1, hallId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                allProjectionsForHall.add(new ProjectionTimeAndDurationDTO(rs.getTimestamp("date_time").toLocalDateTime(),
                        rs.getInt("runtime_in_min")));
            }
        }
        return allProjectionsForHall;
    }
}
