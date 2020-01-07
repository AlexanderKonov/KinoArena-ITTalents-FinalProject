package season11.kino_arena.model.pojo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Cinema {
    private long id;
    private String name;
    private String address;
    private String telephoneNumber;
    private  String city;
    private String cinemaInfo;
}
