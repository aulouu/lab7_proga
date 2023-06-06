package commands;

import exceptions.UserNotFound;
import managers.DatabaseManager;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.sql.SQLException;

/**
 * Внутренняя команда
 */
public class LoginCommand extends Command {
    private DatabaseManager databaseManager;

    public LoginCommand(DatabaseManager databaseManager) {
        super("login", " : войти под именем пользователя (внутренняя команда)");
        this.databaseManager = databaseManager;
    }

    /**
     * Исполнение команды
     *
     * @param request аргументы
     */
    @Override
    public Response execute(Request request) {
        try {
            databaseManager.verifyUser(request.getUser());
        } catch (SQLException exception) {
            return new Response(ResponseStatus.ERROR, "Произошла ошибка при обращении к базе данных.");
        } catch (UserNotFound exception) {
            return new Response(ResponseStatus.AUTH_ERROR, "Неправильные имя пользователя или пароль.");
        }
        return new Response(ResponseStatus.OK,"Пользователь авторизирован.");
    }
}
