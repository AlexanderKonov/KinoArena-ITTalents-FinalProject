package season11.kino_arena.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import season11.kino_arena.model.dto.RegisterUserDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;
    @Column(name = "last_name")
    private String lastName;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    @JsonIgnore
    private String password;
    @Column
    private String city;
    @Column(name = "post_code")
    private int postCode;
    @Column
    private String address;
    @Column
    private String education;
    @Column
    private String job;
    @Column(name = "personal_info")
    private String personalInfo;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "is_admin")
    private Boolean isAdmin;

    public User(RegisterUserDTO u){
        setFirstName(u.getFirstName());
        setSecondName(u.getSecondName());
        setLastName(u.getLastName());
        setUsername(u.getUsername());
        setEmail(u.getEmail());
        setCity(u.getCity());
        setPostCode(u.getPostCode());
        setAddress(u.getAddress());
        setEducation(u.getEducation());
        setJob(u.getJob());
        setPersonalInfo(u.getPersonalInfo());
    }
}
