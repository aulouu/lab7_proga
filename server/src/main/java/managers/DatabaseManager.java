package managers;

import exceptions.UserExist;
import exceptions.UserNotFound;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DatabaseManager {

    private DatabaseHandler databaseHandler;
    static final Logger databaseManagerLogger = LoggerFactory.getLogger(DatabaseManager.class);

    public DatabaseManager(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public boolean checkUser(String login) throws SQLException {
        PreparedStatement preparedCheckUserStatement = null;
        preparedCheckUserStatement = databaseHandler.getPreparedStatement(SQLRequests.GET_USER);
        preparedCheckUserStatement.setString(1, login);
        ResultSet resultSet = preparedCheckUserStatement.executeQuery();
        return resultSet.next();
    }

    public void addUser(User newUser) throws SQLException, UserExist {
        PreparedStatement preparedAddUserStatement = null;
        try {
            preparedAddUserStatement = databaseHandler.getPreparedStatement(SQLRequests.ADD_USER);
            if (this.checkUser(newUser.getLogin())) throw new UserExist();
            preparedAddUserStatement.setString(1, newUser.getLogin());
            preparedAddUserStatement.setString(2, PasswordHasher.hashPassword(newUser.getPassword()));
            if (preparedAddUserStatement.executeUpdate() == 0) throw new SQLException();
            databaseManagerLogger.info("Пользователь " + newUser + "добавлен.");
        } catch (SQLException exception) {
            databaseManagerLogger.error("Произошла ошибка при добавлении пользователя.");
            throw exception;
        } finally {
            databaseHandler.closePreparedStatement(preparedAddUserStatement);
        }
    }

    public void verifyUser(User user) throws SQLException, UserNotFound {
        PreparedStatement preparedVerifyUserStatement = null;
        try {
            preparedVerifyUserStatement = databaseHandler.getPreparedStatement(SQLRequests.GET_USER);
            preparedVerifyUserStatement.setString(1, user.getLogin());
            ResultSet resultSet = preparedVerifyUserStatement.executeQuery();
            if (resultSet.next()) {
                String pass = PasswordHasher.hashPassword(user.getPassword());
                String login = user.getLogin();
                if (pass.equals(resultSet.getString("password")) && login.equals(resultSet.getString("login")))
                    databaseManagerLogger.info("Пользователь " + user + "авторизирован.");
                else throw new UserNotFound();
            } else throw new UserNotFound();
        } catch (SQLException exception) {
            databaseManagerLogger.error("Произошла ошибка при авторизации пользователя.");
            throw exception;
        } finally {
            databaseHandler.closePreparedStatement(preparedVerifyUserStatement);
        }
    }

    public Deque<Worker> readAll() throws SQLException {
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
            }
            return collection;
        } catch (SQLException exception) {
            databaseManagerLogger.error("Произошла ошибка при загрузке коллекции из таблиц бд.");
        } finally {
            databaseHandler.closePreparedStatement(preparedReadAllStatement);
        }
        return null;
    }

    public Worker addObject(Worker worker, User user) throws SQLException {
        Worker newWorker = null;
        PreparedStatement preparedAddObjectStatement = null;
        try {
            LocalDateTime creationTime = LocalDateTime.now();

            preparedAddObjectStatement = databaseHandler.getPreparedStatement(SQLRequests.ADD_OBJECT);
            preparedAddObjectStatement.setString(1, worker.getName());
            preparedAddObjectStatement.setLong(2, worker.getCoordinates().getX());
            preparedAddObjectStatement.setFloat(3, worker.getCoordinates().getY());
            preparedAddObjectStatement.setTimestamp(4, Timestamp.valueOf(creationTime));
            preparedAddObjectStatement.setLong(5, worker.getSalary());
            preparedAddObjectStatement.setObject(6, Date.valueOf(worker.getStartDate()), Types.DATE);
            preparedAddObjectStatement.setObject(7, worker.getPosition(), Types.OTHER);
            preparedAddObjectStatement.setObject(8, worker.getStatus(), Types.OTHER);
            preparedAddObjectStatement.setInt(9, worker.getPerson().getHeight());
            preparedAddObjectStatement.setObject(10, worker.getPerson().getEyeColor(), Types.OTHER);
            preparedAddObjectStatement.setObject(11, worker.getPerson().getNationality(), Types.OTHER);
            preparedAddObjectStatement.setInt(12, worker.getPerson().getLocation().getX());
            preparedAddObjectStatement.setFloat(13, worker.getPerson().getLocation().getY());
            preparedAddObjectStatement.setString(14, worker.getPerson().getLocation().getName());
            preparedAddObjectStatement.setString(15, user.getLogin());

            ResultSet resultSet = preparedAddObjectStatement.executeQuery();
            if (!resultSet.next()) {
                databaseManagerLogger.error("Новый элемент не добавлен в коллекцию.");
            } else {
                newWorker = new Worker(
                        resultSet.getInt(1),
                        worker.getName(),
                        worker.getCoordinates(),
                        creationTime,
                        worker.getSalary(),
                        worker.getStartDate(),
                        worker.getPosition(),
                        worker.getStatus(),
                        worker.getPerson(),
                        user.getLogin()
                );
                databaseManagerLogger.info("Новый элемент добавлен в коллекцию.");
            }
        } catch (SQLException exception) {
            databaseManagerLogger.error("Произошла ошибка при добавлении нового элемент в таблицу.");
            throw exception;
        } finally {
            databaseHandler.closePreparedStatement(preparedAddObjectStatement);
        }
        return newWorker;
    }

    public boolean removeObject(int id, User user) throws SQLException {
        PreparedStatement preparedRemoveObjectStatement = null;
        try {
            preparedRemoveObjectStatement = databaseHandler.getPreparedStatement(SQLRequests.DELETE_OBJECT);
            preparedRemoveObjectStatement.setInt(1, id);
            preparedRemoveObjectStatement.setString(2, user.getLogin());
            ResultSet resultSet = preparedRemoveObjectStatement.executeQuery();
            resultSet.next();
            databaseManagerLogger.info("Элемент удален.");
            return true;
        } catch (SQLException exception) {
            databaseManagerLogger.error("Произошла ошибка при удалении элемента.");
            return false;
        } finally {
            databaseHandler.closePreparedStatement(preparedRemoveObjectStatement);
        }
    }

    public boolean removeAllObjects(List<Integer> ids, User user) throws SQLException {
        PreparedStatement preparedRemoveAllObjectsStatement = null;
        try {
            for (Integer id : ids) {
                preparedRemoveAllObjectsStatement = databaseHandler.getPreparedStatement(SQLRequests.DELETE_OBJECT);
                preparedRemoveAllObjectsStatement.setInt(1, id);
                preparedRemoveAllObjectsStatement.setString(2, user.getLogin());
                ResultSet resultSet = preparedRemoveAllObjectsStatement.executeQuery();
            }
            databaseManagerLogger.info("Объекты, принадлежащие " + user.getLogin() + " удалены.");
            return true;
        } catch (SQLException exception) {
            databaseManagerLogger.error("Произошла ошибка при удалении объектов.");
            return false;
        } finally {
            databaseHandler.closePreparedStatement(preparedRemoveAllObjectsStatement);
        }
    }

    public boolean updateObject(int id, Worker worker, User user) throws SQLException {
        PreparedStatement preparedUpdateObjectStatement = null;
        try {
            LocalDateTime creationTime = LocalDateTime.now();

            preparedUpdateObjectStatement = databaseHandler.getPreparedStatement(SQLRequests.UPDATE_OBJECT);
            preparedUpdateObjectStatement.setString(1, worker.getName());
            preparedUpdateObjectStatement.setLong(2, worker.getCoordinates().getX());
            preparedUpdateObjectStatement.setFloat(3, worker.getCoordinates().getY());
            preparedUpdateObjectStatement.setTimestamp(4, Timestamp.valueOf(creationTime));
            preparedUpdateObjectStatement.setLong(5, worker.getSalary());
            preparedUpdateObjectStatement.setObject(6, Date.valueOf(worker.getStartDate()), Types.DATE);
            preparedUpdateObjectStatement.setObject(7, worker.getPosition(), Types.OTHER);
            preparedUpdateObjectStatement.setObject(8, worker.getStatus(), Types.OTHER);
            preparedUpdateObjectStatement.setInt(9, worker.getPerson().getHeight());
            preparedUpdateObjectStatement.setObject(10, worker.getPerson().getEyeColor(), Types.OTHER);
            preparedUpdateObjectStatement.setObject(11, worker.getPerson().getNationality(), Types.OTHER);
            preparedUpdateObjectStatement.setInt(12, worker.getPerson().getLocation().getX());
            preparedUpdateObjectStatement.setFloat(13, worker.getPerson().getLocation().getY());
            preparedUpdateObjectStatement.setString(14, worker.getPerson().getLocation().getName());
            preparedUpdateObjectStatement.setInt(15, id);
            //preparedUpdateObjectStatement.setString(16, user.getLogin());

            ResultSet resultSet = preparedUpdateObjectStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
            databaseManagerLogger.error("Произошла ошибка при обновлении объекта.");
            return false;
        } finally {
            databaseHandler.closePreparedStatement(preparedUpdateObjectStatement);
        }
    }

}
