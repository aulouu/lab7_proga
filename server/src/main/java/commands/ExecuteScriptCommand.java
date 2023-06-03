package commands;

import exceptions.IllegalArgument;
import work.Request;
import work.Response;
import work.ResponseStatus;

/**
 * Команда, которая считывает и исполняет скрипт из указанного файла
 */

public class ExecuteScriptCommand extends Command {
    public ExecuteScriptCommand() {
        super("execute_script", " file_name : считать и исполнить скрипт из указанного файла");
    }

    /**
     * Исполнение команды
     *
     * @param request аргументы
     * @throws IllegalArgument неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArgument {
        if (request.getArgs() == null || request.getArgs().isBlank()) {
            return new Response(ResponseStatus.ERROR, "Файл со скриптом не найден.");
        }
        String fileName = (request.getArgs().trim());
        return new Response(ResponseStatus.EXECUTE_SCRIPT, fileName);
    }
}
