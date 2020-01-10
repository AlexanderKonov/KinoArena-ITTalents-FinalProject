package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.ProjectionDAO;
import season11.kino_arena.model.dao.TicketDAO;
import season11.kino_arena.model.dao.UserDAO;
import season11.kino_arena.model.dto.*;
import season11.kino_arena.model.pojo.CinemaHall;
import season11.kino_arena.model.pojo.Projection;

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

    @GetMapping("/projection/{projectionID}/tickets/free")
    public ArrayList<TicketWithoutUserDTO> getFreeTicketsForProjection(@PathVariable(name = "projectionID") long projectionID) throws SQLException, NotFoundException {
        ArrayList<TicketWithoutUserDTO> reservedTickets = ticketDAO.getReservedTicketsByProjectionId(projectionID);
        return getFreeTickets(reservedTickets,projectionID);
    }

    @GetMapping("/projection/{projectionID}/tickets/reserved")
    public ArrayList<TicketWithoutUserDTO> getReservedTickets(@PathVariable(name = "projectionID") long projectionID) throws SQLException {
        return ticketDAO.getReservedTicketsByProjectionId(projectionID);
    }

    private ArrayList<TicketWithoutUserDTO> getFreeTickets(ArrayList<TicketWithoutUserDTO> taken , long projectionID) throws NotFoundException, SQLException {
        CinemaHall cinemaHall = projectionDAO.getById(projectionID).getHall();
        boolean[][] hallMatrix = new boolean[cinemaHall.getNumberOfRows()][cinemaHall.getNumberOfSeatsPerRow()];
        for (TicketWithoutUserDTO ticket :
                taken) {
                hallMatrix[ticket.getRowNumber()][ticket.getSeatNumber()] = true;
        }
        ArrayList<TicketWithoutUserDTO> freeTickets = new ArrayList<>();
        for (int row = 0; row < hallMatrix.length; row++) {
            for (int col = 0; col < hallMatrix[0].length; col++) {
                if (!hallMatrix[row][col]){
                    freeTickets.add(new TicketWithoutUserDTO(row,col));
                }
            }
        }
        return freeTickets;
    }
}