package commands;

import exceptions.IllegalArgument;
import exceptions.PermissionDenied;
import managers.CollectionManager;
import managers.DatabaseManager;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.sql.SQLException;

/**
 * Команда, которая удаляет элемент из коллекции, ШВ которого равен заданному
 */

public class RemoveByIDCommand extends Command {
    private CollectionManager collectionManager;
    private DatabaseManager databaseManager;

    public RemoveByIDCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("remove_by_id", " id : удалить элемент из коллекции, ID которого равен заданному");
        this.collectionManager = collectionManager;
        ;
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
            if (!collectionManager.getById(id).getOwner().equals(request.getUser().getLogin())) throw new PermissionDenied();
            if (databaseManager.removeObject(id, request.getUser())) {
                collectionManager.removeElement(collectionManager.getById(id));
                return new Response(ResponseStatus.OK, "Элемент с таким ID удален.");
            } else return new Response(ResponseStatus.ERROR, "Элемент не удален.");
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
