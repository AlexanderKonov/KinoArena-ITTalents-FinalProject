package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
        //TODO validate data in userDto
        //create User object
        if (userIsRegistered(userDto)){
            throw new BadRequestException("This username is already taken.");
        }
        if (!hasValidPassword(userDto)){
            throw new BadRequestException("Invalid password.");
        }
        User user = new User(userDto);
        //add to database
        userDao.addUser(user);
        //return UserWithoutPasswordDTO
        //session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        //UserWithoutPasswordDTO responseDto = new UserWithoutPasswordDTO(user);
        UserWithoutPasswordDTO userWithoutPassword = new UserWithoutPasswordDTO(user);
        return userWithoutPassword;
    }

    @PostMapping("/login")
    public UserWithoutPasswordDTO login (@RequestBody LoginUserDTO loginUserDTO, HttpSession session) throws SQLException {
        User user = userDao.getByUsername(loginUserDTO.getUsername());
        if(user == null){
            //throw new AuthorizationException("Invalid credentials");
            return null;
        }
        else
        if(passwordValid(user, loginUserDTO)) {
            session.setAttribute(SESSION_KEY_LOGGED_USER, user);
            return new UserWithoutPasswordDTO(user);
        }
        else{
            //throw new AuthorizationException("Invalid credentials");
        }
        return null;
    }

    @PostMapping("/logout")
    public void login(HttpSession session){
        session.invalidate();
    }

    private boolean passwordValid(User user, LoginUserDTO userDTO) {
        //TODO validate pass
        return true;
    }

    public boolean userIsRegistered(RegisterUserDTO u) throws SQLException {
        return userDao.getByUsername(u.getUsername())!=null;
    }

    public boolean hasValidPassword(RegisterUserDTO u) {
        return !(!(u.getPassword().equals(u.getConfirmPassword())) || u.getPassword().contains(" ") || u.getPassword().length() < 8);
        //return !u.getPassword().equals(u.getConfirmPassword()) && !u.getPassword().contains(" ") && u.getPassword().length() >= 8;
    }
}
