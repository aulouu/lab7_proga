package commands;

import exceptions.IllegalArgument;
import managers.CollectionManager;
import managers.DatabaseManager;
import models.Worker;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.sql.SQLException;
import java.util.List;

/**
 * Команда, которая очищает коллекцию
 */

public class ClearCommand extends Command {
    private CollectionManager collectionManager;
    private DatabaseManager databaseManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("clear", " : очистить коллекцию");
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
        if (!request.getArgs().isBlank()) throw new IllegalArgument();
        try {
            List<Integer> deleteIds = collectionManager.getCollection().stream()
                    .filter(worker -> worker.getOwner() == null || request.getUser().getLogin().equals(worker.getOwner()))
                    .map(Worker::getId)
                    .toList();
            if (databaseManager.removeAllObjects(deleteIds, request.getUser())) {
                collectionManager.removeElements(deleteIds);
                return new Response(ResponseStatus.OK, "Ваши элементы удалены из коллекции.");
            } else return new Response(ResponseStatus.ERROR, "Элементы коллекции удалить не удалось.");
        } catch (SQLException exception) {
            return new Response(ResponseStatus.ERROR, "Произошла ошибка при обращении к базе данных.");
        }
    }
}
