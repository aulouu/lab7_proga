package managers;

import console.Console;
import console.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseHandler {

    private final String JDBC_DRIVER = "org.postgresql.Driver";
    private String url;
    private String user;
    private String password;
    private Connection connection;
    static final Logger databaseHandlerLogger = LoggerFactory.getLogger(DatabaseHandler.class);

    public DatabaseHandler(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        /*connectToDatabase();
        try {
            createTables();
        } catch (SQLException exception) {
            databaseHandlerLogger.error("Таблицы уже существуют.");
        }*/
    }

    public boolean connectToDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, password);
            databaseHandlerLogger.info("Соединение с базой данных установлено.");
            return true;
        } catch (SQLException exception) {
            databaseHandlerLogger.error("Произошла ошибка при подключении к базе данных.");
        } catch (ClassNotFoundException exception) {
            databaseHandlerLogger.error("Драйвер управления базой данных не найден.");
        }
        return false;
    }

    public void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
            databaseHandlerLogger.info("Соединение с базой данных разорвано.");
        } catch (SQLException exception) {
            databaseHandlerLogger.error("Произошла ошибка при разрыве соединения с базой данных.");
        }
    }

    public PreparedStatement getPreparedStatement(String sqlStatement) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            if (connection == null) throw new SQLException();
            preparedStatement = connection.prepareStatement(sqlStatement);
            return preparedStatement;
        } catch (SQLException exception) {
            if (connection == null)
                databaseHandlerLogger.error("Соединение с базой данных не установлено!");
            throw new SQLException(exception);
        }
    }

    public void closePreparedStatement(PreparedStatement sqlStatement) throws SQLException {
        if (sqlStatement == null) return;
        try {
            sqlStatement.close();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        }
    }

    public boolean createTables() throws SQLException {
        try {
            connection.prepareStatement(SQLRequests.CREATE_TABLES).execute();
            databaseHandlerLogger.info("Таблицы успешно созданы.");
        } catch (SQLException exception) {
            databaseHandlerLogger.error("Произошла ошибка.");
            return false;
        }
        return true;
    }
}
