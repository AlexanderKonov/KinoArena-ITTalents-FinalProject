package season11.kino_arena.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.pojo.Projection;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class ProjectionForTicketDTO {

    private long id;
    private String movie;
    private String cinema;
    private String hall;
    private LocalDateTime dateTime;

    public ProjectionForTicketDTO(Projection p) {
        setId(p.getId());
        setMovie(p.getMovie().getName());
        setCinema(p.getHall().getCinema().getName());
        setHall(p.getHall().getCinemaHallType().getName());
        setDateTime(p.getDateTime());
    }
}
