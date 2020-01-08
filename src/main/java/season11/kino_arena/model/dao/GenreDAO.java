package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.model.pojo.Genre;

import java.sql.*;

@Component
public class GenreDAO {

    private static final String SELECT_BY_ID = "SELECT id, `name` FROM genre WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Genre getById(long id) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Genre(rs.getLong("id"),
                        rs.getString("name"));
            }
            else{
                return null;
            }
        }
    }
}
