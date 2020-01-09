package season11.kino_arena.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.dto.ProjectionDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Projection {

    private long id;
    private Movie movie;
    private CinemaHall hall;
    private LocalDateTime  dateTime;

    public Projection(ProjectionDTO p, Movie m, CinemaHall ch) {
        setId(p.getId());
        setMovie(m);
        setHall(ch);
        setDateTime(p.getDateTime());
    }
}
