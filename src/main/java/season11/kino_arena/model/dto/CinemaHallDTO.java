package season11.kino_arena.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.pojo.Cinema;
import season11.kino_arena.model.pojo.CinemaHallType;


@Getter
@Setter
@NoArgsConstructor
public class CinemaHallDTO {
    private long id;
    private long cinemaHallTypeId;
    private long cinemaId;
    private int numberOfRows;
    private int numberOfSeatsPerRow;
}
