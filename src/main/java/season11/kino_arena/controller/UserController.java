package season11.kino_arena.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import season11.kino_arena.model.dao.UserDAO;
import season11.kino_arena.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class UserController {

    @Autowired
    private UserDAO userDao;
    @PostMapping("/register")
    public User register(@RequestBody User userDto, HttpSession session) throws SQLException {
        //TODO validate data in userDto
        //create User object
        User user = new User(userDto);
        //add to database
        userDao.addUser(user);
        //return UserWithoutPasswordDTO
        //session.setAttribute(SESSION_KEY_LOGGED_USER, user);
        //UserWithoutPasswordDTO responseDto = new UserWithoutPasswordDTO(user);
        return user;
    }


}