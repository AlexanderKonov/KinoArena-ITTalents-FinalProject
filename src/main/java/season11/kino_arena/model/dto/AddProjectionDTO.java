package season11.kino_arena.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProjectionDTO {

    private long id;
    private long movie;
    private long hall;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  dateTime;

}
