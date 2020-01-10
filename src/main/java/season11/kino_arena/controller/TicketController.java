package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.ProjectionDAO;
import season11.kino_arena.model.dao.TicketDAO;
import season11.kino_arena.model.dao.UserDAO;
import season11.kino_arena.model.dto.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class TicketController {

    @Autowired
    private ProjectionDAO projectionDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TicketDAO ticketDAO;

    @PostMapping("/tickets/add")
    public TicketResponseDTO addTicket(@RequestBody TicketDTO ticketDTO) throws SQLException, NotFoundException {
        ticketDAO.addTicket(ticketDTO);
        return new TicketResponseDTO(ticketDTO.getId(),
                new UserForTicketDTO(userDAO.getById(ticketDTO.getUser())),
                new ProjectionForTicketDTO(projectionDAO.getById(ticketDTO.getProjection())),
                ticketDTO.getRowNumber(),
                ticketDTO.getSeatNumber());
    }

    @GetMapping("/users/{id}/tickets")
    public ArrayList<TicketResponseDTO> getAllTicketsForCertainUser(@PathVariable(name = "id") long id) throws SQLException, NotFoundException {
        return ticketDAO.getAllTicketsForCertainUser(id);
    }
}