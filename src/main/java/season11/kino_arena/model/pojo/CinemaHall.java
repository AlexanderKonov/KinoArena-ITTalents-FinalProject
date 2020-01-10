package season11.kino_arena.model.pojo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.CinemaDAO;
import season11.kino_arena.model.dao.CinemaHallTypeDAO;
import season11.kino_arena.model.dto.CinemaHallDTO;

import java.sql.SQLException;

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
