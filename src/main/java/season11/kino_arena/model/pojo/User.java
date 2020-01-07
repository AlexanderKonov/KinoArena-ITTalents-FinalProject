package season11.kino_arena.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.dto.RegisterUserDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private long id;
    private String firstName;
    private String secondName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String city;
    private int postCode;
    private String address;
    private String education;
    private String job;
    private String personalInfo;
    private LocalDateTime createTime;

    public User(User u) {
        setFirstName(u.getFirstName());
        setSecondName(u.getSecondName());
        setLastName(u.getLastName());
        setUsername(u.getUsername());
        setEmail(u.getEmail());
        setPassword(u.getPassword());
        setCity(u.getCity());
        setPostCode(u.getPostCode());
        setAddress(u.getAddress());
        setEducation(u.getEducation());
        setJob(u.getJob());
        setPersonalInfo(u.getPersonalInfo());
        setCreateTime(u.getCreateTime());
    }

    public User(RegisterUserDTO u){
        setFirstName(u.getFirstName());
        setSecondName(u.getSecondName());
        setLastName(u.getLastName());
        setUsername(u.getUsername());
        setEmail(u.getEmail());
        setPassword(u.getPassword());
        setCity(u.getCity());
        setPostCode(u.getPostCode());
        setAddress(u.getAddress());
        setEducation(u.getEducation());
        setJob(u.getJob());
        setPersonalInfo(u.getPersonalInfo());
        //setCreateTime(LocalDateTime.now());
    }
}
