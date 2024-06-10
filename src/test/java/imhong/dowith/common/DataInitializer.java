package imhong.dowith.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer {

    public static final int FIRST_COLUMN = 1;

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager em;

    private final List<String> truncateQueries = new ArrayList<>();

    @Transactional
    void init() {
        if (truncateQueries.isEmpty()) {
            initTruncateQueries();
        }

        em.createNativeQuery("SET foreign_key_checks = 0").executeUpdate();
        truncateQueries.forEach(query -> em.createNativeQuery(query).executeUpdate());
        em.createNativeQuery("SET foreign_key_checks = 1").executeUpdate();
    }

    private void initTruncateQueries() {
        try (final Statement statement = dataSource.getConnection().createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SHOW TABLES ");

            while (resultSet.next()) {
                final String tableName = resultSet.getString(FIRST_COLUMN);
                truncateQueries.add("TRUNCATE TABLE " + tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
