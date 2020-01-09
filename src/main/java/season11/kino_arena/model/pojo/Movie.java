package season11.kino_arena.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.dto.AddMovieDTO;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class Movie {

    private long id;
    private String name;
    private String description;
    private int runtimeInMin;
    private Date premiere;
    private Genre genre;
    private Restriction restriction;
    private double rating;
    private boolean isDubbed;
    private VideoFormat videoFormat;
    private String cast;
    private String director;

    public Movie(AddMovieDTO m, Genre g, Restriction r, VideoFormat vf) {
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
