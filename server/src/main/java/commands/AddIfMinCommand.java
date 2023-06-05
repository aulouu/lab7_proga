package commands;

import exceptions.IllegalArgument;
import managers.CollectionManager;
import managers.DatabaseManager;
import models.Worker;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Команда, которая добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
 */

public class AddIfMinCommand extends Command {
    private CollectionManager collectionManager;
    private DatabaseManager databaseManager;

    public AddIfMinCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("add_if_min", " {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекци");
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
            if (Objects.isNull(request.getObject())) {
                return new Response(ResponseStatus.ASK_OBJECT, "Для команды " + this.getName() + " требуется объект.");
            }
            Worker element = databaseManager.addObject(request.getObject(), request.getUser());
            if (element.compareTo(Objects.requireNonNull(collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .min(Worker::compareTo)
                    .orElse(null))) >= 1) {
                collectionManager.addElement(element);
                return new Response(ResponseStatus.OK, "Элемент успешно добавлен.");
            } else {
                return new Response(ResponseStatus.ERROR, "Элемент не соответствует условиям команды.");
            }
        } catch (SQLException exception) {
            return new Response(ResponseStatus.ERROR, "Произошла ошибка при обращении к базе данных.");
        }
    }
}

