package managers;

import commands.Command;
import exceptions.CommandRuntime;
import exceptions.IllegalArgument;
import exceptions.MustExit;
import exceptions.NoCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.Request;
import work.Response;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Командный менеджер - класс для управления командами
 */

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final DatabaseManager databaseManager;
    static final Logger commandManagerLogger = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Добавляет команду
     *
     * @param commands комманды
     */
    public void addCommand(Collection<Command> commands) {
        this.commands.putAll(commands.stream()
                .collect(Collectors.toMap(Command::getName, с -> с)));
        commandManagerLogger.info("Добавлены команды ", commands);
    }

    /**
     * @param request аргументы
     * @throws IllegalArgument неверные аргументы команды
     * @throws NoCommand       такой команды нет
     * @throws CommandRuntime  ошибка при исполнении команды
     * @throws MustExit        обязательный выход из программы
     */
    public Response execute(Request request) throws IllegalArgument, NoCommand, CommandRuntime, MustExit {
        Command command = commands.get(request.getCommandName());
        if (command == null) throw new NoCommand();
        Response response = command.execute(request);
        return response;
    }
}

