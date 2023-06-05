package commands;

import exceptions.IllegalArgument;
import managers.CollectionManager;
import managers.DatabaseManager;
import models.Worker;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

/**
 * Команда, которая удаляет из коллекции все элементы, ID которых меньше, чем заданный
 */

public class RemoveLowerCommand extends Command {
    private CollectionManager collectionManager;
    private DatabaseManager databaseManager;

    public RemoveLowerCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("remove_lower", " id : удалить из коллекции все элементы, ID которых меньше, чем заданный");
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
        try {
            int id = Integer.parseInt(request.getArgs().trim());
            Collection<Worker> remove = collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(worker -> worker.getId() < id)
                    .filter(worker -> worker.getOwner().equals(request.getUser().getLogin()))
                    .filter(el -> {
                        try {
                            return databaseManager.removeObject(el.getId(), request.getUser());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            for (Worker object : remove)
                collectionManager.removeElement(object);
            if (remove.isEmpty()) {
                return new Response(ResponseStatus.ERROR, "Нет элементов, у которых ID меньше, чем заданный.");
            } else return new Response(ResponseStatus.OK, "Элементы, ID которых меньше, чем заданный, удалены.");
        } catch (NumberFormatException exception) {
            return new Response(ResponseStatus.ERROR, "ID должно быть числом типа integer.");
        }
    }
}
