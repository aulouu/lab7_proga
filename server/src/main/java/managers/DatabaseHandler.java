package managers;

import console.Console;
import console.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseHandler {

    private final String JDBC_DRIVER = "org.postgresql.Driver";
    /*private String url;
    private String user;
    private String password;*/
    private Connection connection;
    private Print console = new Console();
    static final Logger databaseHandlerLogger = LoggerFactory.getLogger(DatabaseHandler.class);

    /*public DatabaseHandler() {
        this.url = url;
        this.user = user;
        this.password = password;
        this.console = console;

        connectToDatabase();
        createTables();
    }*/

    /*private void connectToDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, password);
            console.println("Соединение с базой данных установлено.");
            databaseHandlerLogger.info("Соединение с базой данных установлено.");
        } catch (SQLException exception) {
            console.printError("Произошла ошибка при подключении к базе данных.");
            databaseHandlerLogger.error("Произошла ошибка при подключении к базе данных.");
        } catch (ClassNotFoundException exception) {
            console.printError("Драйвер управления базой данных не найден.");
            databaseHandlerLogger.error("Драйвер управления базой данных не найден.");
        }
    }*/

    public boolean connectToDatabase(String prFile) {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader(prFile)) {
            properties.load(fileReader);
        } catch (FileNotFoundException exception) {
            console.printError("Файл не найден.");
            return false;
        } catch (IOException exception) {
            console.printError("Ошибка при чтении.");
            return false;
        }
        String dbName = properties.getProperty("db");
        if (dbName == null) {
            console.printError("Укажите название базы данных.");
            return false;
        }
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection("jdbc:postgresql:" + dbName, properties);
        } catch (ClassNotFoundException exception) {
            console.printError("Драйвер базы данных не найден.");
            return false;
        } catch (SQLException exception) {
            console.printError("Не удалось подключиться к базе данных.");
            return false;
        }
        console.println("Подключение к базе данных установлено успешно.");
        return true;
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

    public boolean createTables() {
        try {
            connection.prepareStatement(SQLRequests.CREATE_TABLES).execute();
            databaseHandlerLogger.info("Табоицы успешно созданы.");
        } catch (SQLException exception) {
            console.printError("Произошла ошибка.");
            return false;
        }
        return true;
    }
}
