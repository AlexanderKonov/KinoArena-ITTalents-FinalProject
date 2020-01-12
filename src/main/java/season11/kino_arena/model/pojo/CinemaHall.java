package season11.kino_arena.model.pojo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import season11.kino_arena.exceptions.NotFoundException;
import season11.kino_arena.model.dao.CinemaDAO;
import season11.kino_arena.model.dao.CinemaHallTypeDAO;
import season11.kino_arena.model.dto.CinemaHallDTO;

import javax.persistence.*;
import java.sql.SQLException;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cinema_halls")
public class CinemaHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "cinema_hall_type_id")
    private CinemaHallType cinemaHallType;
    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;
    @Column(name = "number_of_rows")
    private int numberOfRows;
    @Column(name = "number_of_seats_per_row")
    private int numberOfSeatsPerRow;

    public CinemaHall(CinemaHallDTO hall, CinemaHallType cinemaHallType, Cinema cinema) {
        this.id = hall.getId();
        this.cinemaHallType = cinemaHallType;
        this.cinema = cinema;
        this.numberOfRows = hall.getNumberOfRows();
        this.numberOfSeatsPerRow = hall.getNumberOfSeatsPerRow();
    }
}
