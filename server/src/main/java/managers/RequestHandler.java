package managers;

import exceptions.CommandRuntime;
import exceptions.IllegalArgument;
import exceptions.MustExit;
import exceptions.NoCommand;
import work.Request;
import work.Response;
import work.ResponseStatus;

public class RequestHandler {
    private CommandManager commandManager;
    private Request request;

    public RequestHandler(Request request, CommandManager commandManager) {
        this.request = request;
        this.commandManager = commandManager;
    }

    public Response handle(Request request) {
        try {
            return commandManager.execute(request);
        } catch (IllegalArgument exception) {
            return new Response(ResponseStatus.WRONG_ARGUMENTS, "Введены неправильные аргументы команды.");
        } catch (CommandRuntime exception) {
            return new Response(ResponseStatus.ERROR, "Ошибка при исполнении программы");
        } catch (NoCommand exception) {
            return new Response(ResponseStatus.ERROR, "Такой команды не существует.");
        } catch (MustExit exception) {
            return new Response(ResponseStatus.EXIT);
        }
    }
}
