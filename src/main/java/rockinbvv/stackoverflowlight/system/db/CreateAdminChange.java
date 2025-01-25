package rockinbvv.stackoverflowlight.system.db;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.RequiredArgsConstructor;
import rockinbvv.stackoverflowlight.system.crypto.EncryptionService;

import java.sql.Statement;

@RequiredArgsConstructor
public class CreateAdminChange implements CustomTaskChange {

    @Override
    public void execute(Database database) {
        EncryptionService encryptionService = new EncryptionService();

        try {
            JdbcConnection dbConn = (JdbcConnection) database.getConnection();

            try {
                Statement updateStatement = dbConn.createStatement();
                updateStatement.execute(String.format("insert into app_user(name, email, password) values ('%s', '%s', '%s')",
                        "VladTheGreat",
                        "vlad.dracula@impaler.com",
                        encryptionService.encrypt("impaler228")
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
