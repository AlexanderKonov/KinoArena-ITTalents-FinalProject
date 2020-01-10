package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dto.*;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dto.TicketDTO;

import java.sql.*;
import java.util.ArrayList;

@Component
public class TicketDAO {

    private static final String DELETE_ALL_TICKETS_BY_PROJECTION_ID = "DELETE FROM tickets WHERE projection_id = ?";
    private static final String GET_ALL_RESERVED_TICKETS_FOR_PROJECTION =
            "SELECT `row_number` , seat_number FROM tickets WHERE projection_id = ?";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProjectionDAO projectionDAO;
    @Autowired
    private UserDAO userDAO;

    private static final String ADD_TICKET_SQL = "INSERT INTO tickets " +
            "(user_id, " +
            "projection_id, " +
            "`row_number`, " +
            "seat_number) " +
            "VALUES (?,?,?,?); ";
    private static final String SELECT_TICKETS_BY_USER_ID = "SELECT " +
            "id, " +
            "user_id, " +
            "projection_id, " +
            "`row_number`, " +
            "seat_number " +
            "FROM tickets WHERE user_id = ?";
    private static final String DELETE_ALL_TICKETS_BY_PROJECTION_ID = "DELETE FROM tickets WHERE projection_id = ?";
    private static final String DELETE_ALL_TICKETS_BY_ID = "DELETE FROM tickets WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProjectionDAO projectionDAO;
    @Autowired
    private UserDAO userDAO;

    public void addTicket(TicketDTO ticketDTO) throws SQLException {
        try (
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(ADD_TICKET_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, ticketDTO.getUser());
            ps.setLong(2, ticketDTO.getProjection());
            ps.setInt(3, ticketDTO.getRowNumber());
            ps.setInt(4, ticketDTO.getSeatNumber());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            ticketDTO.setId(keys.getLong(1));
        }
    }

    public ArrayList<TicketResponseDTO> getAllTicketsForCertainUser(long id) throws SQLException, NotFoundException {
        ArrayList<TicketResponseDTO> tickets = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_TICKETS_BY_USER_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TicketDTO ticketDTO = new TicketDTO(rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getLong("projection_id"),
                        rs.getInt("row_number"),
                        rs.getInt("seat_number"));

                tickets.add(
                        new TicketResponseDTO(ticketDTO.getId(),
                                new UserForTicketDTO(userDAO.getById(ticketDTO.getUser())),
                                new ProjectionForTicketDTO(projectionDAO.getById(ticketDTO.getProjection())),
                                ticketDTO.getRowNumber(),
                                ticketDTO.getSeatNumber()));
            }
        }
        return tickets;
    }

    public void deleteTicketsByProjectionId(long projectionId) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(DELETE_ALL_TICKETS_BY_PROJECTION_ID)){
            ps.setLong(1,projectionId);
            ps.executeUpdate();
        }
    }

    public void deleteTicketById(long id) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(DELETE_ALL_TICKETS_BY_ID)){
            ps.setLong(1,id);
            ps.executeUpdate();

    public ArrayList<TicketWithoutUserDTO> getReservedTicketsByProjectionId(long projectionId) throws SQLException {
        ArrayList<TicketWithoutUserDTO> allReservedTickets = new ArrayList<>();
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_ALL_RESERVED_TICKETS_FOR_PROJECTION)){
            ps.setLong(1, projectionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                allReservedTickets.add(new TicketWithoutUserDTO(rs.getInt("row_number"),
                        rs.getInt("seat_number")));
            }
        }
        return allReservedTickets;
    }
}
