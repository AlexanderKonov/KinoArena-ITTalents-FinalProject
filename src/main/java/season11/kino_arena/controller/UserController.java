package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
    public User register(@RequestBody RegisterUserDTO userDto, HttpSession session) throws SQLException {
        //TODO validate data in userDto
        //create User object
        if (!userDto.hasValidPassword()){
            //throw new InvalidPasswordException();
        }
        User user = new User(userDto);
        //add to database
        userDao.addUser(user);
        //return UserWithoutPasswordDTO
        //session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        //UserWithoutPasswordDTO responseDto = new UserWithoutPasswordDTO(user);
        return user;
    }

    @PostMapping("/login")
    public UserWithoutPasswordDTO login (@RequestBody LoginUserDTO loginUserDTO, HttpSession session) throws SQLException {
        User user = userDao.getByUsername(loginUserDTO.getUsername());
        if(user == null){
            //throw new AuthorizationException("Invalid credentials");
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


    private boolean passwordValid(User user, LoginUserDTO userDTO) {
        //TODO validate pass
        return true;
    }

    @PostMapping("/logout")
    public void login(HttpSession session){
        session.invalidate();
    }

}
