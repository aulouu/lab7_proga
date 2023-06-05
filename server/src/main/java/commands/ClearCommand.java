package commands;

import exceptions.IllegalArgument;
import managers.CollectionManager;
import managers.DatabaseManager;
import work.Request;
import work.Response;
import work.ResponseStatus;

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
        collectionManager.clear();
        return new Response(ResponseStatus.OK, "Коллекция очищена.");
    }
}
