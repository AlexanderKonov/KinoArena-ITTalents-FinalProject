package season11.kino_arena.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {

    private long id;
    private UserForTicketDTO user;
    private ProjectionForTicketDTO projection;
    private int rowNumber;
    private int seatNumber;

}
