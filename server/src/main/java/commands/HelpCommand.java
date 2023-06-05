package commands;

import exceptions.IllegalArgument;
import managers.CommandManager;
import work.Request;
import work.Response;
import work.ResponseStatus;

/**
 * Команда, которая выводит информацию о всех остальных командах
 */

public class HelpCommand extends Command {
    private CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        super("help", " : вывести информацию о всех остальных командах");
        this.commandManager = commandManager;
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
        return new Response(ResponseStatus.OK,
                "add {element} : добавить новый элемент в коллекцию \n" +
                        "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекци \n" +
                        "clear : очистить коллекцию \n" +
                        "execute_script {file_name} : считать и исполнить скрипт из указанного файла \n" +
                        "exit : завершить программу без сохранения в файл \n" +
                        "filter_by_status {status} : вывести элементы, значение поля status которых равно заданному \n" +
                        "filter_starts_with_name {name} : вывести элементы, значение поля name которых начинается с заданной подстроки \n" +
                        "head : вывести первый элемент коллекции \n" +
                        "help : вывести информацию о всех остальных командах \n" +
                        "info : вывести информацию о коллекции \n" +
                        "remove_by_id {id} : удалить элемент из коллекции, ID которого равен заданному \n" +
                        "remove_greater {element} : удалить из коллекции элементы, превышающие заданный \n" +
                        "remove_lower {id} : удалить из коллекции все элементы, ID которых меньше, чем заданный \n" +
                        "show : вывести все элементы коллекции в строковом представлении \n" +
                        "update id {element} : обновить значение элемента коллекции, ID которого равен заданному"
                );
    }
}
