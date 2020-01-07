package season11.kino_arena.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Movie {
    private long id;
    private String name;
    private String description;
    private int runtimeInMin;
    private LocalDate premiereDate;
    private Genre genre;
    private Restriction restriction;
    private int rating;
    private boolean isDubbed;
    private VideoFormat videoFormat;
    private String cast;
    private String director;
}
