package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import season11.kino_arena.exceptions.AuthorizationException;
import season11.kino_arena.exceptions.BadRequestException;
import season11.kino_arena.model.dao.UserDAO;
import season11.kino_arena.model.dto.LoginUserDTO;
import season11.kino_arena.model.dto.RegisterUserDTO;
import season11.kino_arena.model.dto.UserWithoutPasswordDTO;
import season11.kino_arena.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class UserController {

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    @Autowired
    private UserDAO userDao;
    @PostMapping("/register")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDto, HttpSession session) throws SQLException, BadRequestException {
        validateUserData(userDto);
        User user = new User(userDto);
        userDao.addUser(user);
        session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        return new UserWithoutPasswordDTO(userDao.getById(user.getId()));
    }

    @PostMapping("/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO loginUserDTO, HttpSession session) throws SQLException {
        User user = userDao.getByUsername(loginUserDTO.getUsername());
        if(user == null){
            throw new AuthorizationException("Invalid credentials");
        }
        else
        if(passwordValid(user, loginUserDTO)) {
            session.setAttribute(SESSION_KEY_LOGGED_USER, user);
            return new UserWithoutPasswordDTO(user);
        }
        else{
            throw new AuthorizationException("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public void logout(HttpSession session){
        session.invalidate();
    }

    private boolean passwordValid(User user, LoginUserDTO userDTO) {
        //TODO validate pass
        return true;
    }

    private boolean isUsernameTaken(RegisterUserDTO u) throws SQLException {
        return userDao.getByUsername(u.getUsername()) != null ;
    }

    private boolean isEmailTaken(RegisterUserDTO u) throws SQLException {
        return userDao.getByEmail(u.getEmail()) != null;
    }

    private boolean hasValidPassword(String password) {
        int min = 8;
        int max = 16;
        int digit = 0;
        int special = 0;
        int upCount = 0;
        int loCount = 0;
        if(password.length() >= min && password.length() <= max){
            for(int i = 0; i < password.length(); i++){
                char c = password.charAt(i);
                if(Character.isUpperCase(c)){
                    upCount++;
                }
                if(Character.isLowerCase(c)){
                    loCount++;
                }
                if(Character.isDigit(c)){
                    digit++;
                }
                if(c >= 33 && c <= 46 || c==64){
                    special++;
                }
            }
        }
        return special >= 1 && loCount >= 1 && upCount >= 1 && digit >= 1;
    }

    private void validateUserData(RegisterUserDTO userDto) throws BadRequestException, SQLException {
        if (isEmailTaken(userDto)){
            throw new BadRequestException("This email is taken.");
        }
        if (isUsernameTaken(userDto)){
            throw new BadRequestException("This username is taken.");
        }
        if(!userDto.getPassword().equals(userDto.getConfirmPassword())){
            throw new BadRequestException("Both passwords must be the same");
        }
        if (!hasValidPassword(userDto.getPassword())){
            throw new BadRequestException("Password must be between 8 and 16 characters and must contain " +
                    "one or more uppercase characters, " +
                    "one or more lowercase characters, " +
                    "one or more digits, " +
                    "one or more special characters.");
        }
    }
}
