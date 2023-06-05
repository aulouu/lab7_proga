package commands;

import exceptions.IllegalArgument;
import managers.CollectionManager;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Команда, которая выводит элементы, значение поля name которых начинается с заданной подстроки
 */

public class FilterStartsWithNameCommand extends Command {
    private CollectionManager collectionManager;

    public FilterStartsWithNameCommand(CollectionManager collectionManager) {
        super("filter_starts_with_name", " {name} : вывести элементы, значение поля name которых начинается с заданной подстроки");
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
        String result = collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .filter(worker -> worker.getName().startsWith(request.getArgs()))
                .map(Objects::toString).collect(Collectors.joining(", "));
        if(result.isEmpty())
            return  new Response(ResponseStatus.ERROR, "Нет таких элементов.");
        return new Response(ResponseStatus.OK, "Соответствующие элементы: " + result);
    }
}

