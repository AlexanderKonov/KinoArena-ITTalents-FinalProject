package season11.kino_arena.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.dto.CinemaHallDTO;

@Getter
@Setter
@NoArgsConstructor
public class CinemaHall {

    private long id;
    private CinemaHallType cinemaHallType;
    private Cinema cinema;
    private int numberOfRows;
    private int numberOfSeatsPerRow;

    public CinemaHall(CinemaHallDTO hall, CinemaHallType cinemaHallType, Cinema cinema) {
        this.id = hall.getId();
        this.cinemaHallType = cinemaHallType;
        this.cinema = cinema;
        this.numberOfRows = hall.getNumberOfRows();
        this.numberOfSeatsPerRow = hall.getNumberOfSeatsPerRow();
    }
}
