package season11.kino_arena.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Projection {
    private long id;
    private Movie movie;
    private CinemaHall hall;
    private LocalDateTime dateTime;
}
