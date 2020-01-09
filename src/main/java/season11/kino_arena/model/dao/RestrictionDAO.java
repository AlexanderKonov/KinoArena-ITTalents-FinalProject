package season11.kino_arena.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import season11.kino_arena.model.pojo.Restriction;

import java.sql.*;

@Component
public class RestrictionDAO {

    private static final String SELECT_BY_ID = "SELECT id, `name` FROM restrictions WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Restriction getById(long id) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        try(PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Restriction(rs.getLong("id"),
                        rs.getString("name"));
            }
            else{
                return null;
            }
        }
    }
}