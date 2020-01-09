package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.model.dto.TicketDTO;

import java.sql.*;

@Component
public class TicketDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String ADD_TICKET_SQL = "INSERT INTO tickets " +
                                                    "(user_id, " +
                                                    "projection_id, " +
                                                    "`row_number`, " +
                                                    "seat_number) " +
                                                    "VALUES (?,?,?,?); ";

    public void addTicket(TicketDTO ticketDTO) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(ADD_TICKET_SQL, Statement.RETURN_GENERATED_KEYS)) {
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

}
