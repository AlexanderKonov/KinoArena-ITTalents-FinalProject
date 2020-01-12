package season11.kino_arena.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CinemaHallDTO {

    private long id;
    private long cinemaHallTypeId;
    private long cinemaId;
    private int numberOfRows;
    private int numberOfSeatsPerRow;
}
