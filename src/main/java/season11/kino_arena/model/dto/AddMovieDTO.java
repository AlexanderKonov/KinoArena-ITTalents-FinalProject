package season11.kino_arena.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddMovieDTO {

    private long id;
    private String name;
    private String description;
    private int runtimeInMin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date premiere;
    private long genre;
    private long restriction;
    private double rating;
    private Boolean isDubbed;
    private long videoFormat;
    private String cast;
    private String director;

}
