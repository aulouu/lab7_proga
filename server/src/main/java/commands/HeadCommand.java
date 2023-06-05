package commands;

import exceptions.IllegalArgument;
import managers.CollectionManager;
import work.Request;
import work.Response;
import work.ResponseStatus;

/**
 * Команда, которая выводит первый элемент коллекции
 */

public class HeadCommand extends Command {
    private CollectionManager collectionManager;

    public HeadCommand(CollectionManager collectionManager) {
        super("head", " : вывести первый элемент коллекции");
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
        if (collectionManager.getCollection().isEmpty()) {
            return new Response(ResponseStatus.ERROR, "Коллекция пустая.");
        } else {
            return new Response(ResponseStatus.OK, "Первый элемент коллекции: " +
                    collectionManager.head());
        }
    }
}
