package season11.kino_arena.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.dto.ProjectionDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projections")
public class Projection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "cinema_hall_id")
    private CinemaHall hall;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time")
    private LocalDateTime  dateTime;

    public Projection(ProjectionDTO p, Movie m, CinemaHall ch) {
        setId(p.getId());
        setMovie(m);
        setHall(ch);
        setDateTime(p.getDateTime());
    }
}
