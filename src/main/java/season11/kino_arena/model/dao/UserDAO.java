package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.model.pojo.User;

import java.sql.*;
import java.time.LocalDateTime;

@Component
public class UserDAO {

    private static final String REGISTER_USER_SQL = "INSERT INTO users (" +
            "first_name, " +
            "second_name, " +
            "last_name, " +
            "username, " +
            "email, " +
            "password, " +
            "city, " +
            "post_code, " +
            "address, " +
            "education, " +
            "job, " +
            "personal_info, " +
            "create_time) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(User user) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = connection.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getSecondName());
        ps.setString(3, user.getLastName());
        ps.setString(4, user.getUsername());
        ps.setString(5, user.getEmail());
        ps.setString(6, user.getPassword());
        ps.setString(7, user.getCity());
        ps.setInt(8, user.getPostCode());
        ps.setString(9, user.getAddress());
        ps.setString(10, user.getEducation());
        ps.setString(11, user.getJob());
        ps.setString(12, user.getPersonalInfo());
        ps.setTimestamp(13,Timestamp.valueOf(LocalDateTime.now()));
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        keys.next();
        user.setId(keys.getLong(1));
    }
}