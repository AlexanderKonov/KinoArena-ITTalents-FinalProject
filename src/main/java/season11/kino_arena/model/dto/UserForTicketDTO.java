package season11.kino_arena.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.pojo.User;

@Setter
@Getter
@NoArgsConstructor
public class UserForTicketDTO {

    private long id;
    private String firstName;
    private String secondName;
    private String lastName;

    public UserForTicketDTO(User u) {
        setId(u.getId());
        setFirstName(u.getFirstName());
        setSecondName(u.getSecondName());
        setLastName(u.getLastName());
    }
}
