package managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseHandler {

    private final String JDBC_DRIVER = "org.postgresql.Driver";
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/studs";
    public static final String DATABASE_URL_HELIOS = "jdbc:postgresql://pg:5432/studs";
    String dbProps = "db.cfg";
    private Connection connection;
    public boolean isConnect = false;
    static final Logger databaseHandlerLogger = LoggerFactory.getLogger(DatabaseHandler.class);

    public DatabaseHandler() {
        try {
            this.connectToDatabase(dbProps);
            if (isConnect)
                this.createTables();
        } catch (SQLException exception) {
            databaseHandlerLogger.error("Таблицы уже существуют.");
        }
    }

    public void connectToDatabase(String propertiesFile) {
        Properties properties = null;
        try {
            properties = new Properties();
            try (FileReader fr = new FileReader(propertiesFile)) {
                properties.load(fr);
            } catch (IOException exception) {
                databaseHandlerLogger.error("Ошибка в чтении конфинга базы данных.");
                isConnect = false;
            }
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            databaseHandlerLogger.info("Соединение с базой данных установлено.");
            isConnect = true;
        } catch (SQLException exception) {
            try {
                connection = DriverManager.getConnection(DATABASE_URL_HELIOS, properties);
            } catch (SQLException ex) {
                databaseHandlerLogger.error("Произошла ошибка при подключении к базе данных.");
                isConnect = false;
            }
        } catch (ClassNotFoundException exception) {
            databaseHandlerLogger.error("Драйвер управления базой данных не найден.");
            isConnect = false;
        }
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

    public void createTables() throws SQLException {
        connection.prepareStatement(SQLRequests.CREATE_TABLES).execute();
        databaseHandlerLogger.info("Таблицы успешно созданы.");
    }
}
