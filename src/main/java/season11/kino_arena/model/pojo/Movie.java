package season11.kino_arena.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.dto.MovieDTO;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column(name = "runtime_in_min")
    private int runtimeInMin;
    @Temporal(TemporalType.DATE)
    private Date premiere;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    @ManyToOne
    @JoinColumn(name = "restriction_id")
    private Restriction restriction;
    @Column
    private double rating;
    @Column
    private boolean isDubbed;
    @ManyToOne
    @JoinColumn(name = "video_format_id")
    private VideoFormat videoFormat;
    @Column
    private String cast;
    @Column(name = "directors")
    private String director;

    public Movie(MovieDTO m, Genre g, Restriction r, VideoFormat vf) {
        setId(m.getId());
        setName(m.getName());
        setDescription(m.getDescription());
        setRuntimeInMin(m.getRuntimeInMin());
        setPremiere(m.getPremiere());
        setGenre(g);
        setRestriction(r);
        setRating(m.getRating());
        setDubbed(m.getIsDubbed());
        setVideoFormat(vf);
        setCast(m.getCast());
        setDirector(m.getDirector());
    }
}
