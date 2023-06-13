package commands;

import exceptions.IllegalArgument;
import exceptions.PermissionDenied;
import managers.CollectionManager;
import managers.DatabaseManager;
import models.Worker;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Команда, которая обновляет значение элемента коллекции, ID которого равен заданному
 */

public class UpdateCommand extends Command {
    private CollectionManager collectionManager;
    private DatabaseManager databaseManager;

    public UpdateCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("update", " id {element} : обновить значение элемента коллекции, ID которого равен заданному");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }

    /**
     * Исполнение команды
     *
     * @param request аргументы
     * @throws IllegalArgument неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArgument {
        if (request.getArgs().isBlank()) throw new IllegalArgument();
        class NoId extends RuntimeException {
        }
        try {
            int id = Integer.parseInt(request.getArgs().trim());
            if (!collectionManager.checkId(id)) throw new NoId();
            if (!collectionManager.checkPermission(id, request.getUser().getLogin())) throw new PermissionDenied();
            if (Objects.isNull(request.getObject())) {
                return new Response(ResponseStatus.ASK_OBJECT, "Для команды " + this.getName() + " требуется объект.");
            }
            if (databaseManager.updateObject(id, request.getObject(), request.getUser())) {
                collectionManager.editById(id, request.getObject());
                return new Response(ResponseStatus.OK, "Элемент обновлен.");
            } else return new Response(ResponseStatus.ERROR, "Элемент не обновлен.");
        } catch (NoId exception) {
            return new Response(ResponseStatus.ERROR, "В коллекции нет элемента с таким ID.");
        } catch (NumberFormatException exception) {
            return new Response(ResponseStatus.ERROR, "ID должно быть числом типа integer.");
        } catch (PermissionDenied exception) {
            return new Response(ResponseStatus.ERROR, "Недостаточно прав для выполнения данной команды.");
        } catch (SQLException exception) {
            return new Response(ResponseStatus.ERROR, "Произошла ошибка при обращении к базе данных.");
        }
    }
}
