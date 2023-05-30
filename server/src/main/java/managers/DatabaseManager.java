package managers;

import console.Print;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DatabaseManager {
    private DatabaseHandler databaseHandler;
    private Print console;
    static final Logger databaseManagerLogger = LoggerFactory.getLogger(DatabaseManager.class);

    public DatabaseManager(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public boolean checkUser(String login) throws SQLException {
        PreparedStatement preparedCheckUserStatement = null;
        try {
            preparedCheckUserStatement = databaseHandler.getPreparedStatement(SQLRequests.GET_USER);
            preparedCheckUserStatement.setString(1, login);
            ResultSet resultSet = preparedCheckUserStatement.executeQuery();
            resultSet.next();
            return true;
        } catch (SQLException exception) {
            console.printError("Произошла ошибка при проверке пользователя.");
        }
        finally {
            databaseHandler.closePreparedStatement(preparedCheckUserStatement);
        }
        return false;
    }

    public void addUser (User user) throws SQLException {
        PreparedStatement preparedAddUserStatement = null;
        String login = user.getName();
        String pass = user.getPassword();
        try {
            preparedAddUserStatement = databaseHandler.getPreparedStatement(SQLRequests.ADD_USER);
            if (this.checkUser(login)) throw new SQLException();
            preparedAddUserStatement.setString(1, login);
            preparedAddUserStatement.setString(2, PasswordHasher.hashPassword(pass));
            preparedAddUserStatement.execute();
            databaseManagerLogger.info("Пользователь " + user + " добавлен.");
        } catch (SQLException exception) {
            console.printError("Произошло ошибка при добавлении пользователя.");
        }
        finally {
            databaseHandler.closePreparedStatement(preparedAddUserStatement);
        }
    }

    public boolean verifyUser (User newUser) throws SQLException {
        PreparedStatement preparedVerifyUserStatement = null;
        String login = newUser.getName();
        try {
            preparedVerifyUserStatement = databaseHandler.getPreparedStatement(SQLRequests.GET_USER);
            preparedVerifyUserStatement.setString(1, login);
            ResultSet resultSet = preparedVerifyUserStatement.executeQuery();
            if (resultSet.next()) {
                String pass = PasswordHasher.hashPassword(newUser.getPassword());
                return pass.equals(resultSet.getString("password"));
            } else return false;
        } catch (SQLException exception) {
            console.printError("Произошло ошибка при подтверждении пользователя.");
        }
        finally {
            databaseHandler.closePreparedStatement(preparedVerifyUserStatement);
        }
        return false;
    }

    public ArrayDeque<Worker> readAll() throws SQLException {
        PreparedStatement preparedReadAllStatement = null;
        try {
            preparedReadAllStatement = databaseHandler.getPreparedStatement(SQLRequests.GET_OBJECTS);
            ResultSet resultSet = preparedReadAllStatement.executeQuery();
            Deque<Worker> collection = new ConcurrentLinkedDeque<>();
            while (resultSet.next()) {
                collection.add(new Worker(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        new Coordinates(
                                resultSet.getLong("coordinate_x"),
                                resultSet.getFloat("coordinate_y")
                        ),
                        resultSet.getTimestamp("creation_date").toLocalDateTime(),
                        resultSet.getLong("salary"),
                        resultSet.getDate("start_date").toLocalDate(),
                        Position.valueOf(resultSet.getString("position")),
                        Status.valueOf(resultSet.getString("status")),
                        new Person(
                                resultSet.getInt("person_height"),
                                Color.valueOf(resultSet.getString("person_eye_color")),
                                Country.valueOf(resultSet.getString("person_nationality")),
                                new Location(
                                        resultSet.getInt("person_location_x"),
                                        resultSet.getFloat("person_location_y"),
                                        resultSet.getString("person_location_name")
                                )
                        ),
                        resultSet.getString("owner")
                ));
                databaseManagerLogger.info("Коллекция загружена.");
                return (ArrayDeque<Worker>) collection;
            }
        } catch (SQLException exception) {
            console.printError("Произошло ошибка при загрузке коллекции из таблиц бд. Возможно коллекция пустая.");
        }
        finally {
            databaseHandler.closePreparedStatement(preparedReadAllStatement);
        }
        return new ArrayDeque<>();
    }

    public boolean addObject(Worker worker, User user) throws SQLException {
        PreparedStatement preparedAddObjectStatement = null;
        try {
            LocalDateTime creationTime = LocalDateTime.now();

            preparedAddObjectStatement = databaseHandler.getPreparedStatement(SQLRequests.ADD_OBJECT);
            preparedAddObjectStatement.setString(1, worker.getName());
            preparedAddObjectStatement.setLong(2, worker.getCoordinates().getX());
            preparedAddObjectStatement.setFloat(3, worker.getCoordinates().getY());
            preparedAddObjectStatement.setTimestamp(4, Timestamp.valueOf(creationTime));
            preparedAddObjectStatement.setLong(5, worker.getSalary());
            preparedAddObjectStatement.setDate(6, Date.valueOf(worker.getStartDate()));
            preparedAddObjectStatement.setString(7, worker.getPosition().name());
            preparedAddObjectStatement.setString(8, worker.getStatus().name());
            preparedAddObjectStatement.setInt(9, worker.getPerson().getHeight());
            preparedAddObjectStatement.setString(10, worker.getPerson().getEyeColor().name());
            preparedAddObjectStatement.setString(11, worker.getPerson().getNationality().name());
            preparedAddObjectStatement.setInt(12, worker.getPerson().getLocation().getX());
            preparedAddObjectStatement.setFloat(13, worker.getPerson().getLocation().getY());
            preparedAddObjectStatement.setString(14, worker.getPerson().getLocation().getName());
            preparedAddObjectStatement.setString(15, user.getName());

            if (preparedAddObjectStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSet = preparedAddObjectStatement.executeQuery();
            if(!resultSet.next()) {
                databaseManagerLogger.error("Новый объект не добавлен.");
                return false;
            } else databaseManagerLogger.info("Новый объект добавлен.");
            return true;
        } catch (SQLException exception) {
            console.printError("Произошла ошибка при добавлении нового объекта в таблицу.");
        }
        finally {
            databaseHandler.closePreparedStatement(preparedAddObjectStatement);
        }
        return false;
    }

    public boolean removeObject(int id, User user) throws SQLException {
        PreparedStatement preparedRemoveObjectStatement = null;
        try {
            preparedRemoveObjectStatement = databaseHandler.getPreparedStatement(SQLRequests.DELETE_OBJECT);
            preparedRemoveObjectStatement.setInt(1, id);
            preparedRemoveObjectStatement.setString(2, user.getName());
            ResultSet resultSet = preparedRemoveObjectStatement.executeQuery();
            resultSet.next();
            databaseManagerLogger.info("Объект удален.");
            return true;
        } catch (SQLException exception) {
            console.printError("Произошла ошибка при удалении объекта.");
        }
        finally {
            databaseHandler.closePreparedStatement(preparedRemoveObjectStatement);
        }
        return false;
    }

    public boolean removeAllObjects(int id, User user) throws SQLException {
        PreparedStatement preparedRemoveAllObjectsStatement = null;
        try {
            preparedRemoveAllObjectsStatement = databaseHandler.getPreparedStatement(SQLRequests.DELETE_OBJECT);
            preparedRemoveAllObjectsStatement.setInt(1, id);
            preparedRemoveAllObjectsStatement.setString(2, user.getName());
            ResultSet resultSet = preparedRemoveAllObjectsStatement.executeQuery();
            resultSet.next();
            databaseManagerLogger.info("Объекты, принадлежащие " + user.getName() + " удалены.");
            return true;
        } catch (SQLException exception) {
            console.printError("Произошла ошибка при удалении всех объектов.");
        }
        finally {
            databaseHandler.closePreparedStatement(preparedRemoveAllObjectsStatement);
        }
        return false;
    }

    public boolean updateObject(int id, Worker worker, User user) throws SQLException {
        PreparedStatement preparedUpdateObjectStatement = null;
        try {
            LocalDateTime creationTime = LocalDateTime.now();

            preparedUpdateObjectStatement = databaseHandler.getPreparedStatement(SQLRequests.UPDATE_OBJECT);
            preparedUpdateObjectStatement.setInt(1, id);
            preparedUpdateObjectStatement.setString(2, worker.getName());
            preparedUpdateObjectStatement.setLong(3, worker.getCoordinates().getX());
            preparedUpdateObjectStatement.setFloat(4, worker.getCoordinates().getY());
            preparedUpdateObjectStatement.setTimestamp(5, Timestamp.valueOf(creationTime));
            preparedUpdateObjectStatement.setLong(6, worker.getSalary());
            preparedUpdateObjectStatement.setDate(7, Date.valueOf(worker.getStartDate()));
            preparedUpdateObjectStatement.setString(8, worker.getPosition().name());
            preparedUpdateObjectStatement.setString(9, worker.getStatus().name());
            preparedUpdateObjectStatement.setInt(10, worker.getPerson().getHeight());
            preparedUpdateObjectStatement.setString(11, worker.getPerson().getEyeColor().name());
            preparedUpdateObjectStatement.setString(12, worker.getPerson().getNationality().name());
            preparedUpdateObjectStatement.setInt(13, worker.getPerson().getLocation().getX());
            preparedUpdateObjectStatement.setFloat(14, worker.getPerson().getLocation().getY());
            preparedUpdateObjectStatement.setString(15, worker.getPerson().getLocation().getName());
            preparedUpdateObjectStatement.setString(16, user.getName());

            ResultSet resultSet = preparedUpdateObjectStatement.executeQuery();
            resultSet.next();
            return true;
        } catch (SQLException exception) {
            console.printError("Произошла ошибка при обновлении объекта.");
        }
        finally {
            databaseHandler.closePreparedStatement(preparedUpdateObjectStatement);
        }
        return false;
    }
}
