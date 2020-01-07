package season11.kino_arena.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import season11.kino_arena.model.dao.UserDAO;

import java.sql.SQLException;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserDTO {
    private String firstName;
    private String secondName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String city;
    private int postCode;
    private String address;
    private String education;
    private String job;
    private String personalInfo;

}
