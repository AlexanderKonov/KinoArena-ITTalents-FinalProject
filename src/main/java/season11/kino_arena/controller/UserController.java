package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import season11.kino_arena.exceptions.AuthorizationException;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.model.dao.UserDAO;
import season11.kino_arena.model.dto.LoginUserDTO;
import season11.kino_arena.model.dto.MessageDTO;
import season11.kino_arena.model.dto.RegisterUserDTO;
import season11.kino_arena.model.dto.UserWithoutPasswordDTO;
import season11.kino_arena.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class UserController {

    public static final String LOGGED_USER = "logged_user";
    public static final int MIN_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 15;

    @Autowired
    private UserDAO userDao;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDto, HttpSession session) throws SQLException {
        validateUserData(userDto);
        User user = new User(userDto);
        user.setPassword(encoder.encode(userDto.getPassword()));
        userDao.addUser(user);
        session.setAttribute(LOGGED_USER, user);
        return new UserWithoutPasswordDTO(userDao.getById(user.getId()));
    }

    @PostMapping("/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO loginUserDTO, HttpSession session) throws SQLException {
        if (session.getAttribute(LOGGED_USER) != null){
            throw new BadRequestException("User is already logged. Please log out.");
        }
        User user = userDao.getByUsername(loginUserDTO.getUsername());
        if(user == null){
            throw new AuthorizationException("Invalid credentials");
        }
        if(BCrypt.checkpw(loginUserDTO.getPassword(), user.getPassword())) {
            session.setAttribute(LOGGED_USER, user);
            return new UserWithoutPasswordDTO(user);
        }
        else{
            throw new AuthorizationException("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public MessageDTO logout(HttpSession session){
        session.invalidate();
        return new MessageDTO("Logout successful");
    }

    private boolean isUsernameTaken(RegisterUserDTO u) throws SQLException {
        return userDao.getByUsername(u.getUsername()) != null ;
    }

    private boolean isEmailTaken(RegisterUserDTO u) throws SQLException {
        return userDao.getByEmail(u.getEmail()) != null;
    }

    private boolean hasValidPassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@*#$%^&+=])(?=\\S+$).{8,}$");
    }

    private boolean isUsernameCorrect(RegisterUserDTO userDto) {
        return userDto.getUsername().matches("^[a-zA-Z0-9]+$");
    }

    private boolean isEmailCorrect(RegisterUserDTO userDto) {
        return userDto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void validateUserData(RegisterUserDTO userDto) throws SQLException {
        if (!isEmailCorrect(userDto)){
            throw new BadRequestException("Wrong email");
        }
        if (isEmailTaken(userDto)){
            throw new BadRequestException("This email is taken.");
        }
        if (!isUsernameCorrect(userDto)){
            throw new BadRequestException("Username must contain only characters and numbers.");
        }
        if (isUsernameTaken(userDto)){
            throw new BadRequestException("This username is taken.");
        }
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())){
            throw new BadRequestException("Both passwords must be the same.");
        }
        if (!hasValidPassword(userDto.getPassword())){
            throw new BadRequestException("Password must be between 8 and 16 characters and must contain " +
                    "one or more uppercase characters, " +
                    "one or more lowercase characters, " +
                    "one or more digits, " +
                    "one or more special characters.");
        }
        if (userDto.getFirstName().length() > MAX_NAME_LENGTH || userDto.getFirstName().length() < MIN_LENGTH ||
            userDto.getSecondName().length() > MAX_NAME_LENGTH || userDto.getSecondName().length() < MIN_LENGTH ||
            userDto.getLastName().length() > MAX_NAME_LENGTH || userDto.getLastName().length() < MIN_LENGTH ||
            userDto.getUsername().length() > MAX_NAME_LENGTH || userDto.getUsername().length() < MIN_LENGTH ||
            userDto.getCity().length() > MAX_NAME_LENGTH || userDto.getCity().length() < MIN_LENGTH ||
            userDto.getAddress().length() > MAX_NAME_LENGTH *3 || userDto.getAddress().length() < MIN_LENGTH ||
            userDto.getEducation().length() > MAX_NAME_LENGTH || userDto.getEducation().length() < MIN_LENGTH ||
            userDto.getJob().length() > MAX_NAME_LENGTH || userDto.getJob().length() < MIN_LENGTH ||
            userDto.getPersonalInfo().length() > MAX_NAME_LENGTH *6 || userDto.getPersonalInfo().length() < MIN_LENGTH){
            throw new BadRequestException("Please check your data");
        }
    }

}
