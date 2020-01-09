package season11.kino_arena.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.dto.AddProjectionDTO;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Projection {

    private long id;
    private Movie movie;
    private CinemaHall hall;
    private LocalDateTime  dateTime;

    public Projection(AddProjectionDTO p, Movie m, CinemaHall ch) {
        setId(p.getId());
        setMovie(m);
        setHall(ch);
        setDateTime(p.getDateTime());
    }
}
