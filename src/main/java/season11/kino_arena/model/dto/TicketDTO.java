package season11.kino_arena.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private long id;
    private long user;
    private long projection;
    private int rowNumber;
    private int seatNumber;
}
