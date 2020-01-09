package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import season11.kino_arena.model.dao.ProjectionDAO;
import season11.kino_arena.model.dao.TicketDAO;
import season11.kino_arena.model.dao.UserDAO;
import season11.kino_arena.model.dto.TicketDTO;

import java.sql.SQLException;

@RestController
public class TicketController {

    @Autowired
    private ProjectionDAO projectionDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TicketDAO ticketDAO;

    @PostMapping("/tickets/add")
    public TicketDTO addTicket(@RequestBody TicketDTO ticketDTO) throws SQLException {
        ticketDAO.addTicket(ticketDTO);
        //TODO make return ticket, not ticketDTO
        return ticketDTO;
    }

}
