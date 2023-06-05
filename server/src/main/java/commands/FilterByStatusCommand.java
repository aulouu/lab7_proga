package commands;

import exceptions.IllegalArgument;
import managers.CollectionManager;
import models.Status;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Команда, которая выводит элементы, значение поля status которых равно заданному
 */

public class FilterByStatusCommand extends Command {
    private CollectionManager collectionManager;

    public FilterByStatusCommand(CollectionManager collectionManager) {
        super("filter_by_status", " {status} : вывести элементы, значение поля status которых равно заданному");
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
        if (request.getArgs().isBlank()) throw new IllegalArgument();
        try {
            var status = Status.valueOf(request.getArgs().trim());
            String result = collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(worker -> worker.getStatus().equals(status))
                    .map(Objects::toString).collect(Collectors.joining(", "));
            if(result.isEmpty())
                return  new Response(ResponseStatus.ERROR, "Нет таких элементов.");
            return new Response(ResponseStatus.OK, "Элементы с заданным статусом: " + result);
        } catch (IllegalArgumentException exception) {
            return new Response(ResponseStatus.ERROR, "Такой статус недоступен.");
        }
    }
}
