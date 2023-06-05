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
 * Команда, которая добавляет новый элемент в коллекцию
 */

public class AddElementCommand extends Command {
    private CollectionManager collectionManager;
    private DatabaseManager databaseManager;

    public AddElementCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("add", " {element} : добавить новый элемент в коллекцию");
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
            } else {
                collectionManager.addElement(databaseManager.addObject(request.getObject(), request.getUser()));
                return new Response(ResponseStatus.OK, "Элемент успешно добавлен.");
            }
        } catch (SQLException exception) {
            return new Response(ResponseStatus.ERROR, "Произошла ошибка при обращении к базе данных.");
        }
    }
}
