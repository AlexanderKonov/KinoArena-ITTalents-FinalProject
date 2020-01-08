package season11.kino_arena.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Restriction {
    private long id;
    private String name;
    private int minAge;
}
