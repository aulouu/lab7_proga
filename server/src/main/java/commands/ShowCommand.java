package commands;

import exceptions.IllegalArgument;
import managers.CollectionManager;
import models.Worker;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.util.Collection;

/**
 * Команда выводит все элементы коллекции в строковом представлении
 */

public class ShowCommand extends Command {
    private CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", " : вывести все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
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
        Collection<Worker> collection = collectionManager.getCollection();
        if (collection == null || collection.isEmpty()) {
            return new Response(ResponseStatus.ERROR, "Коллекция еще не инициализирована.");
        }
        return new Response(ResponseStatus.OK, "Ваша коллекция: ", collection);
    }
}
