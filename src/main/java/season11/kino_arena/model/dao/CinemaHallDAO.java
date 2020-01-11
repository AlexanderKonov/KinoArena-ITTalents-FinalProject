package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dto.CinemaHallDTO;
import season11.kino_arena.model.pojo.CinemaHall;

import java.sql.*;
import java.util.ArrayList;

@Component
public class CinemaHallDAO {

    private static final String ADD_CINEMA_HALL_SQL = "INSERT INTO cinema_halls " +
                                                        "(cinema_hall_type_id, " +
                                                        "cinema_id, " +
                                                        "number_of_rows, " +
                                                        "number_of_seats_per_row)" +
                                                        "VALUES (? , ? , ? , ?)";
    private static final String EDIT_CINEMA_HALL_SQL ="UPDATE cinema_halls " +
                                                        "SET " +
                                                        "cinema_hall_type_id = ?, " +
                                                        "cinema_id = ?, " +
                                                        "number_of_rows = ?, " +
                                                        "number_of_seats_per_row = ? " +
                                                        "WHERE " +
                                                        "id=?";
    private static final String DELETE_CINEMA_HALL_SQL = "DELETE FROM cinema_halls WHERE id= ?;";
    private static final String SELECT_BY_ID = "SELECT " +
            "id, " +
            "cinema_hall_type_id, " +
            "cinema_id, " +
            "number_of_rows, " +
            "number_of_seats_per_row " +
            "FROM cinema_halls WHERE id = ?";
    private static final String DELETE_ALL_HALLS_BY_CINEMA_ID = "DELETE FROM cinema_halls WHERE cinema_id = ? ";
    private static final String GET_ALL_HALL_IDS_FOR_CINEMA = "SELECT id FROM cinema_halls WHERE cinema_id = ? ";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    CinemaHallTypeDAO cinemaHallTypeDAO;
    @Autowired
    CinemaDAO cinemaDAO;
    @Autowired
    ProjectionDAO projectionDAO;

    public void addCinemaHall(CinemaHallDTO cinemaHall) throws SQLException {
        try(
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(ADD_CINEMA_HALL_SQL, Statement.RETURN_GENERATED_KEYS)) {
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
        try(
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(EDIT_CINEMA_HALL_SQL)){
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
        projectionDAO.deleteProjectionsByHallId(id);
        try(
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_CINEMA_HALL_SQL)){
            ps.setLong(1,id);
            if (ps.executeUpdate()==0) {
                throw new NotFoundException("Cinema hall was not found.");
            }
        }
    }

    public void deleteCinemaHallsByCinemaId(long cinemaId) throws SQLException {
        ArrayList<Long> cinemaIdList = getAllCinemaHallIdsForCinema(cinemaId);
        for (long id :
                cinemaIdList) {
            projectionDAO.deleteProjectionsByHallId(id);
        }
        try(
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_ALL_HALLS_BY_CINEMA_ID)){
            ps.setLong(1,cinemaId);
            ps.executeUpdate();
        }
    }

    private ArrayList<Long> getAllCinemaHallIdsForCinema(long cinemaId) throws SQLException {
        ArrayList<Long> hallIds = new ArrayList<>();
        try(
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(GET_ALL_HALL_IDS_FOR_CINEMA)){
            ps.setLong(1, cinemaId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                hallIds.add(rs.getLong("id"));
            }
        }
        return hallIds;
    }

    public CinemaHall getById(long id) throws SQLException, NotFoundException {
        try(
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                CinemaHallDTO cinemaHallDTO = new CinemaHallDTO(rs.getLong("id"),
                        rs.getLong("cinema_hall_type_id"),
                        rs.getLong("cinema_id"),
                        rs.getInt("number_of_rows"),
                        rs.getInt("number_of_seats_per_row"));
                return new CinemaHall(cinemaHallDTO,
                        cinemaHallTypeDAO.getCinemaHallTypeById(cinemaHallDTO.getCinemaHallTypeId()),
                        cinemaDAO.getCinemaById(cinemaHallDTO.getCinemaId()));
            }
            else{
                return null;
            }
        }
    }
}
