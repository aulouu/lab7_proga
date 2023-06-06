package commands;

import exceptions.UserExist;
import managers.DatabaseManager;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.sql.SQLException;

/**
 * Внутренняя команда
 */
public class RegisterCommand extends Command {
    private DatabaseManager databaseManager;

    public RegisterCommand(DatabaseManager databaseManager) {
        super("register", " : зарегистрировать пользователя (внутренняя команда)");
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
            databaseManager.addUser(request.getUser());
        } catch (SQLException exception) {
            return new Response(ResponseStatus.ERROR, "Произошла ошибка при обращении к базе данных.");
        } catch (UserExist exception) {
            return new Response(ResponseStatus.AUTH_ERROR, "Такой пользователь уже существует.");
        }
        return new Response(ResponseStatus.OK,"Пользователь успешно зарегистрирован.");
    }
}
