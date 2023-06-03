package commands;

import work.Request;
import work.Response;
import work.ResponseStatus;

public class PingCommand extends Command {
    public PingCommand() {
        super("ping", " : пингануть сервер");
    }

    /**
     * Исполнение команды
     *
     * @param request аргументы
     */
    @Override
    public Response execute(Request request) {
        return new Response(ResponseStatus.OK, "OK");
    }
}
