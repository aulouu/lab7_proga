package managers;

import console.Console;
import console.Print;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseHandler {

    public static final String USER_TABLE = "user";
    public static final String WORKER_TABLE = "worker";
    public static final String COORDINATES_TABLE = "coordinates";
    public static final String PERSON_TABLE = "person";
    public static final String LOCATION_TABLE = "location";

    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_LOGIN_COLUMN = "login";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";

    public static final String COORDINATES_TABLE_ID_COLUMN = "id";
    public static final String COORDINATES_TABLE_WORKER_ID_COLUMN = "worker_id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";

    public static final String LOCATION_TABLE_ID_COLUMN = "id";
    public static final String LOCATION_TABLE_PERSON_ID_COLUMN = "person_id";
    public static final String LOCATION_TABLE_X_COLUMN = "x";
    public static final String LOCATION_TABLE_Y_COLUMN = "y";
    public static final String LOCATION_TABLE_NAME_COLUMN = "name";

    public static final String PERSON_TABLE_HEIGHT_COLUMN = "height";
    public static final String PERSON_TABLE_EYE_COLOR_COLUMN = "eye_color";
    public static final String PERSON_TABLE_NATIONALITY_COLUMN = "nationality";

    public static final String WORKER_TABLE_ID_COLUMN = "id";
    public static final String WORKER_TABLE_NAME_COLUMN = "name";
    public static final String WORKER_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String WORKER_TABLE_SALARY_COLUMN = "salary";
    public static final String WORKER_TABLE_START_DATE_COLUMN = "start_date";
    public static final String WORKER_TABLE_POSITION_COLUMN = "position";
    public static final String WORKER_TABLE_STATUS_COLUMN = "status";
    public static final String WORKER_TABLE_PERSON_ID_COLUMN = "person_id";
    public static final String WORKER_TABLE_USER_ID_COLUMN = "user_id";

    private final String JDBC_DRIVER = "org.postgresql.Driver";
    private String url;
    private String user;
    private String password;
    private Connection connection;
    private Print console;
    static final Logger databaseHandlerLogger = LoggerFactory.getLogger(DatabaseHandler.class);

    public DatabaseHandler(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.console = new Console();

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
            console.println("Соединение с базой данных установлено.");
            databaseHandlerLogger.info("Соединение с базой данных установлено.");
            return true;
        } catch (SQLException exception) {
            console.printError("Произошла ошибка при подключении к базе данных.");
            databaseHandlerLogger.error("Произошла ошибка при подключении к базе данных.");
        } catch (ClassNotFoundException exception) {
            console.printError("Драйвер управления базой данных не найден.");
            databaseHandlerLogger.error("Драйвер управления базой данных не найден.");
        }
        return false;
    }

    public void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
            console.println("Соединение с базой данных разорвано.");
            databaseHandlerLogger.info("Соединение с базой данных разорвано.");
        } catch (SQLException exception) {
            console.printError("Произошла ошибка при разрыве соединения с базой данных.");
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
            exception.printStackTrace();
            console.printError("Произошла ошибка.");
            return false;
        }
        return true;
    }
}
