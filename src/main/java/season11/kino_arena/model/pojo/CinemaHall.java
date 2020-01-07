package season11.kino_arena.model.pojo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CinemaHall {
    private long id;
    private CinemaHallType cinemaHallType;
    private Cinema cinema;
    private int numberOfRows;
    private int numberOfSeatsPerRow;
}
