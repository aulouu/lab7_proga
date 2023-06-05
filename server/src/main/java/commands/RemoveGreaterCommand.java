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
 * Команда, которая удаляет из коллекции все элементы, превышающие заданный
 */

public class RemoveGreaterCommand extends Command {
    private CollectionManager collectionManager;
    private DatabaseManager databaseManager;

    public RemoveGreaterCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("remove_greater", " {element} : удалить из коллекции элементы, превышающие заданный");
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
        if (Objects.isNull(request.getObject())) {
            return new Response(ResponseStatus.ASK_OBJECT, "Для команды " + this.getName() + " требуется объект.");
        }
        Worker element = request.getObject();
        Collection<Worker> remove = collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .filter(worker -> worker.compareTo(element) >= 1)
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
            return new Response(ResponseStatus.ERROR, "Нет элементов, превышающие заданный.");
        } else return new Response(ResponseStatus.OK, "Элементы, превышающие заданный, удалены.");
    }
}
