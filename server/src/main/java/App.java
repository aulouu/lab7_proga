import commands.ExecuteScriptCommand;
import commands.ExitCommand;
import commands.PingCommand;
import commands.RegisterCommand;
import console.Console;
import console.Print;
import managers.*;

import java.util.List;

public class App {
    public static int port = 6090;
    public static final int connection_timeout = 60 * 1000;
    private static final Print console = new Console();
    private static String url = "jdbc:postgresql://localhost:5433/studs";
    private static String user = "s373317";
    private static String password = "";

    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
                console.printError("Порт должен быть числом.");
            }
        }

        DatabaseHandler databaseHandler = new DatabaseHandler(url, user, password);
        DatabaseManager databaseManager = new DatabaseManager(databaseHandler);
        CollectionManager collectionManager = new CollectionManager(databaseManager);
        CommandManager commandManager = new CommandManager(databaseManager);

        commandManager.addCommand(List.of(
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new PingCommand(),
                new RegisterCommand(databaseManager)
        ));

        Server server = new Server(port, connection_timeout, commandManager, databaseManager);
        server.runServer();
        databaseHandler.closeConnection();
    }
}
