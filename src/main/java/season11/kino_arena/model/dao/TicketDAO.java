package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dto.TicketDTO;

import java.sql.*;

@Component
public class TicketDAO {

    private static final String DELETE_ALL_TICKETS_BY_PROJECTION_ID = "DELETE FROM tickets WHERE projection_id = ?";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String ADD_TICKET_SQL = "INSERT INTO tickets " +
                                                    "(user_id, " +
                                                    "projection_id, " +
                                                    "`row_number`, " +
                                                    "seat_number) " +
                                                    "VALUES (?,?,?,?); ";

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

    public void deleteTickets(long projectionId) throws SQLException {
        try(
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_ALL_TICKETS_BY_PROJECTION_ID)){
                ps.setLong(1,projectionId);
                ps.executeUpdate();
        }
    }

}
