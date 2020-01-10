package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dto.MovieDTO;
import season11.kino_arena.model.dto.ProjectionDTO;
import season11.kino_arena.model.dto.ProjectionTimeAndDurationDTO;

import java.sql.*;
import java.util.ArrayList;

@Component
public class ProjectionDAO {

    private static final String GET_ALL_PROJECTIONS_FOR_HALL = "SELECT date_time, runtime_in_min FROM projections " +
            "JOIN movies " +
            "WHERE cinema_hall_id = ?";

    private static final String ADD_PROJECTION_SQL = "INSERT INTO projections (" +
            "movie_id, " +
            "cinema_hall_id, " +
            "date_time) " +
            "VALUES (?,?,?);";
    private static final String EDIT_PROJECTION_SQL = "UPDATE projections " +
                                                        "SET movie_id = ?, " +
                                                        "cinema_hall_id = ?, " +
                                                        "date_time = ? " +
                                                        "WHERE id = ?";
    private static final String DELETE_PROJECTION_SQL = "DELETE FROM projections WHERE id= ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addProjection(ProjectionDTO projection) throws SQLException {
        try (
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(ADD_PROJECTION_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, projection.getMovie());
            ps.setLong(2, projection.getHall());
            ps.setTimestamp(3, Timestamp.valueOf(projection.getDateTime()));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            projection.setId(keys.getLong(1));
        }
    }

    public void editProjection(ProjectionDTO projection) throws SQLException, BadRequestException {
        try (
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(EDIT_PROJECTION_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, projection.getMovie());
            ps.setLong(2, projection.getHall());
            ps.setTimestamp(3, Timestamp.valueOf(projection.getDateTime()));
            ps.setLong(4, projection.getId());
            if(ps.executeUpdate() == 0){
                throw new BadRequestException("Projection with this id doesn`t exist");
            }
        }
    }
    public ArrayList<ProjectionTimeAndDurationDTO> getAllProjectionsForHall(long hallId) throws SQLException {
        ArrayList<ProjectionTimeAndDurationDTO> allProjectionsForHall = new ArrayList<>();
        try(
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(GET_ALL_PROJECTIONS_FOR_HALL)){
            ps.setLong(1, hallId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                allProjectionsForHall.add(new ProjectionTimeAndDurationDTO(rs.getTimestamp("date_time").toLocalDateTime(),
                        rs.getInt("runtime_in_min")));
            }
        }
        return allProjectionsForHall;
    }

    public void deleteProjection(long projectionId) throws SQLException, NotFoundException {
        try(
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_PROJECTION_SQL)){
            ps.setLong(1,projectionId);
            if (ps.executeUpdate()==0) {
                throw new NotFoundException("Projection was not found.");
            }
        }
    }
}
