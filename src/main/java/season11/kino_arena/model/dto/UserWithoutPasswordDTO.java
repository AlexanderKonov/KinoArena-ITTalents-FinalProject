package season11.kino_arena.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
public class UserWithoutPasswordDTO {

    private long id;
    private String firstName;
    private String secondName;
    private String lastName;
    private String username;
    private String email;
    private String city;
    private int postCode;
    private String address;
    private String education;
    private String job;
    private String personalInfo;
    private LocalDateTime createTime;
}
