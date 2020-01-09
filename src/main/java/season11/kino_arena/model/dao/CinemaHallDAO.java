package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dto.CinemaHallDTO;

import java.sql.*;

@Component
public class CinemaHallDAO {

    private static final String ADD_CINEMA_HALL_SQL = "INSERT INTO cinema_halls (cinema_hall_type_id , cinema_id , number_of_rows , number_of_seats_per_row)\n" +
            "VALUES (? , ? , ? , ?)";
    private static final String EDIT_CINEMA_HALL_SQL =
            "UPDATE cinema_halls " +
            "SET " +
            "cinema_hall_type_id = ?, " +
            "cinema_id = ?, " +
            "number_of_rows = ?, " +
            "number_of_seats_per_row = ? " +
            "WHERE " +
            "id=?";
    private static final String DELETE_CINEMA_HALL_SQL = "DELETE FROM cinema_halls WHERE id= ?;";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addCinemaHall(CinemaHallDTO cinemaHall) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement ps = connection.prepareStatement(ADD_CINEMA_HALL_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, cinemaHall.getCinemaHallTypeId());
            ps.setLong(2, cinemaHall.getCinemaId());
            ps.setInt(3, cinemaHall.getNumberOfRows());
            ps.setInt(4, cinemaHall.getNumberOfSeatsPerRow());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            cinemaHall.setId(keys.getLong(1));
        }
    }

    public void updateCinemaHall(CinemaHallDTO cinemaHall) throws SQLException, BadRequestException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement ps = connection.prepareStatement(EDIT_CINEMA_HALL_SQL)){
            ps.setLong(1,cinemaHall.getCinemaHallTypeId());
            ps.setLong(2,cinemaHall.getCinemaId());
            ps.setInt(3,cinemaHall.getNumberOfRows());
            ps.setInt(4,cinemaHall.getNumberOfSeatsPerRow());
            ps.setLong(5,cinemaHall.getId());
            if(ps.executeUpdate()==0){
                throw new BadRequestException("This cinema hall does not exist.");
            }
        }
    }

    public void deleteCinemaHall(long id) throws SQLException, NotFoundException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement ps = connection.prepareStatement(DELETE_CINEMA_HALL_SQL)){
            ps.setLong(1,id);
            if (ps.executeUpdate()==0) {
                throw new NotFoundException("Cinema hall was not found.");
            }
        }
    }


}
