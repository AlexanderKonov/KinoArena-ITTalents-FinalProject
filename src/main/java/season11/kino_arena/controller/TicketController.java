package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import season11.kino_arena.exceptions.AuthorizationException;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.model.dao.ProjectionDAO;
import season11.kino_arena.model.dao.TicketDAO;
import season11.kino_arena.model.dao.UserDAO;
import season11.kino_arena.model.dto.*;
import season11.kino_arena.model.pojo.CinemaHall;
import season11.kino_arena.model.pojo.Projection;
import season11.kino_arena.model.pojo.User;

import javax.servlet.http.HttpSession;
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
    public TicketResponseDTO addTicket(@RequestBody TicketDTO ticketDTO, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        if(!user.getIsAdmin()){
            if(user.getId() != ticketDTO.getUser()){
                throw new AuthorizationException("You don`t have permissions for that");
            }
        }
        validateTicketDTO(ticketDTO);
        ticketDTO.setRowNumber(ticketDTO.getRowNumber()-1);
        ticketDTO.setSeatNumber(ticketDTO.getSeatNumber()-1);
        if(ticketDAO.tickedIsReserved(ticketDTO)){
            throw new BadRequestException("Ticket is already reserved.");
        }
        ticketDAO.addTicket(ticketDTO);
        return new TicketResponseDTO(ticketDTO.getId(),
                new UserForTicketDTO(userDAO.getById(ticketDTO.getUser())),
                new ProjectionForTicketDTO(projectionDAO.getById(ticketDTO.getProjection())),
                ticketDTO.getRowNumber()+1,
                ticketDTO.getSeatNumber()+1);
    }

    private void validateTicketDTO(TicketDTO ticketDTO) throws SQLException {
        Projection projection = projectionDAO.getById(ticketDTO.getProjection());
        User user = userDAO.getById(ticketDTO.getUser());
        if(user == null){
            throw new BadRequestException("Invalid user.");
        }
        if(projection == null){
            throw new BadRequestException("Invalid projection.");
        }
        if (ticketDTO.getRowNumber()<1||ticketDTO.getRowNumber()>projection.getHall().getNumberOfRows()){
            throw new BadRequestException("Ticket row is invalid.");
        }
        if (ticketDTO.getSeatNumber()<1||ticketDTO.getSeatNumber()>projection.getHall().getNumberOfSeatsPerRow()){
            throw new BadRequestException("Ticket seat is invalid.");
        }
    }

    @GetMapping("/users/{id}/tickets")
    public ArrayList<TicketResponseDTO> getAllTicketsForCertainUser(@PathVariable(name = "id") long id, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        if(!user.getIsAdmin()){
            if(user.getId() != id){
                throw new AuthorizationException("You don`t have permissions for that");
            }
        }
        ArrayList<TicketResponseDTO> ticketList = ticketDAO.getAllTicketsForCertainUser(id);
        fixIndexesOfTicketResponseDtoList(ticketList);
        return ticketList;
    }

    @DeleteMapping("/tickets/{id}")
    public MessageDTO deleteTicket(@PathVariable(name = "id") long id, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You don`t have permissions for that");
        }
        if(!user.getIsAdmin()){
            long ticketUserId = ticketDAO.getUserIdByTicketId(id);
            if(user.getId() != ticketUserId){
                throw new AuthorizationException("You don`t have permissions for that");
            }
        }
        ticketDAO.deleteTicketById(id);
        return new MessageDTO("Ticket was deleted successfully.");
    }

    @GetMapping("/projection/{projectionID}/tickets/free")
    public ArrayList<TicketWithoutUserDTO> getFreeTicketsForProjection(
            @PathVariable(name = "projectionID") long projectionID, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        ArrayList<TicketWithoutUserDTO> reservedTickets = ticketDAO.getReservedTicketsByProjectionId(projectionID);
        ArrayList<TicketWithoutUserDTO> ticketList =  getFreeTickets(reservedTickets,projectionID);
        fixIndexesOfTicketWithoutUserDtoList(ticketList);
        return ticketList;
    }

    @GetMapping("/projection/{projectionID}/tickets/reserved")
    public ArrayList<TicketWithoutUserDTO> getReservedTickets(
            @PathVariable(name = "projectionID") long projectionID, HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(UserController.LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        ArrayList<TicketWithoutUserDTO> ticketList =  ticketDAO.getReservedTicketsByProjectionId(projectionID);
        fixIndexesOfTicketWithoutUserDtoList(ticketList);
        return ticketList;
    }

    private ArrayList<TicketWithoutUserDTO> getFreeTickets(ArrayList<TicketWithoutUserDTO> taken , long projectionID) throws SQLException {
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

    private void fixIndexesOfTicketWithoutUserDtoList(ArrayList<TicketWithoutUserDTO> ticketList){
        for (TicketWithoutUserDTO ticket :
                ticketList) {
            ticket.setRowNumber(ticket.getRowNumber()+1);
            ticket.setSeatNumber(ticket.getSeatNumber()+1);
        }
    }

    private void fixIndexesOfTicketResponseDtoList(ArrayList<TicketResponseDTO> ticketList) {
        for (TicketResponseDTO ticket :
                ticketList) {
            ticket.setRowNumber(ticket.getRowNumber()+1);
            ticket.setSeatNumber(ticket.getSeatNumber()+1);
        }
    }
}