package rockinbvv.stackoverflowlight.system.db;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Statement;

@RequiredArgsConstructor
public class CreateAdminChange implements CustomTaskChange {

    @Override
    public void execute(Database database) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

        try {
            JdbcConnection dbConn = (JdbcConnection) database.getConnection();

            try {
                Statement updateStatement = dbConn.createStatement();

                // Insert admin user
                updateStatement.execute(String.format("insert into app_user(name, email, is_admin) values ('%s', '%s', %b)",
                        "VladTheGreat",
                        "vlad.dracula@impaler.com",
                        true
                ));

                // Get the admin user ID
                var resultSet = updateStatement.executeQuery("select id from app_user where email = 'vlad.dracula@impaler.com'");
                resultSet.next();
                long adminId = resultSet.getLong("id");

                // Insert admin password auth
                updateStatement.execute(String.format("insert into user_auth(id_user, auth_type, password) values (%d, '%s', '%s')",
                        adminId,
                        "PASSWORD",
                        passwordEncoder.encode("impaler228")
                ));

                updateStatement.close();
            } catch (Exception e) {
                throw new CustomChangeException(e);
            }
        } catch (CustomChangeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public void setUp() throws SetupException {
    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
        // do nothing since we don't need additional resources
    }
}
