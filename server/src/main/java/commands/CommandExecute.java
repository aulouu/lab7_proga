package commands;

import exceptions.CommandRuntime;
import exceptions.IllegalArgument;
import exceptions.MustExit;
import work.Request;
import work.Response;

/**
 * Интерфейс, реализующий Command Pattern
 */

public interface CommandExecute {
    Response execute(Request request) throws IllegalArgument, CommandRuntime, MustExit;
}
