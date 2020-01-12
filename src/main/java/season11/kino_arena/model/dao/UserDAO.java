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
            "`first_name`, " +
            "`second_name`, " +
            "`last_name`, " +
            "`username`, " +
            "`email`, " +
            "`password`, " +
            "`city`, " +
            "`post_code`, " +
            "`address`, " +
            "`education`, " +
            "`job`, " +
            "`personal_info`, " +
            "`create_time`) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";

    private static final String SELECT_USER_BY_USERNAME = "SELECT * " +
                                                            "FROM users " +
                                                            "WHERE username = ?;";

    private static final String SELECT_USER_BY_EMAIL = "SELECT * " +
                                                            "FROM users " +
                                                            "WHERE email = ?;";

    private static final String SELECT_USER_BY_ID = "SELECT * " +
                                                        "FROM users " +
                                                        "WHERE id = ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(User user) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
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
            ps.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            user.setId(keys.getLong(1));
        }
    }

    public User getByUsername(String username) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_USERNAME, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new User(rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("second_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("city"),
                        rs.getInt("post_code"),
                        rs.getString("address"),
                        rs.getString("education"),
                        rs.getString("job"),
                        rs.getString("personal_info"),
                        rs.getTimestamp("create_time").toLocalDateTime(),
                        rs.getBoolean("is_admin"));
            }
            else{
                return null;
            }
        }
    }

    public User getById(long id) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new User(rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("second_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("city"),
                        rs.getInt("post_code"),
                        rs.getString("address"),
                        rs.getString("education"),
                        rs.getString("job"),
                        rs.getString("personal_info"),
                        rs.getTimestamp("create_time").toLocalDateTime(),
                        rs.getBoolean("is_admin"));
            }
            else{
                return null;
            }
        }
    }

    public User getByEmail(String email) throws SQLException {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_EMAIL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new User(rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("second_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("city"),
                        rs.getInt("post_code"),
                        rs.getString("address"),
                        rs.getString("education"),
                        rs.getString("job"),
                        rs.getString("personal_info"),
                        rs.getTimestamp("create_time").toLocalDateTime(),
                        rs.getBoolean("is_admin"));
            }
            else{
                return null;
            }
        }
    }
}