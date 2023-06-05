package commands;

import exceptions.IllegalArgument;
import managers.CollectionManager;
import work.Request;
import work.Response;
import work.ResponseStatus;

/**
 * Команда, которая выводит информацию  о коллекции
 */

public class InfoCommand extends Command {
    private CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", " : вывести информацию о коллекции");
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
        String lastInitTime = (collectionManager.getLastInitTime() == null)
                ? "В сессии коллекция не инициализирована."
                : collectionManager.getLastInitTime();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Информация о коллекции: \n")
                .append("Тип коллекции: " + collectionManager.collectionType() + "\n")
                .append("Размер коллекции (кол-во элементов): " + collectionManager.collectionSize() + "\n")
                .append("Дата инициализации: " + collectionManager.getLastInitTime() + "\n");
        return new Response(ResponseStatus.OK, stringBuilder.toString());
    }
}
