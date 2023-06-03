package commands;

import managers.DatabaseManager;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.sql.SQLException;

public class RegisterCommand extends Command {
    private DatabaseManager databaseManager;

    public RegisterCommand(DatabaseManager databaseManager) {
        super("register", " : зарегистрировать пользователя");
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
            return new Response(ResponseStatus.AUTH_ERROR, "Такой пользователь уже существует, либо введен неправильй логин.");
        }
        return new Response(ResponseStatus.OK,"Вы успешно зарегистрированы");
    }
}
