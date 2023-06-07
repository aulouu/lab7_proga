import commands.*;
import console.Console;
import console.Print;
import exceptions.IllegalArgument;
import exceptions.WrongPort;
import managers.*;

import java.util.List;

public class App {
    public static int port;
    public static final int connection_timeout = 60 * 1000;
    private static final Print console = new Console();
    private static String url;
    private static String host;
    private static String user = "s373317";
    private static String password;

    private static boolean initializeConnectionAddress(String[] args) {
        try {
            if (args.length != 3)
                throw new IllegalArgument("В аргументы командной строки необходимо передать port db_host db_password.");
            port = Integer.parseInt(args[0]);
            if (port < 0) throw new WrongPort();
            host = args[1];
            password = args[2];
            url = "jdbc:postgresql://" + host + ":5433/studs";
            return true;
        } catch (IllegalArgument exception) {
            console.printError(exception.getMessage());
        } catch (NumberFormatException exception) {
            console.printError("Порт должен быть числом.");
        } catch (WrongPort exception) {
            console.printError("Порт не может быть отрицательным.");
        }
        return false;
    }

    public static void main(String[] args) {
        if (!initializeConnectionAddress(args)) return;
        DatabaseHandler databaseHandler = new DatabaseHandler(url, user, password);
        DatabaseManager databaseManager = new DatabaseManager(databaseHandler);
        CollectionManager collectionManager = new CollectionManager(databaseManager, databaseHandler);
        CommandManager commandManager = new CommandManager(databaseManager);

        commandManager.addCommand(List.of(
                new AddElementCommand(collectionManager, databaseManager),
                new AddIfMinCommand(collectionManager, databaseManager),
                new ClearCommand(collectionManager, databaseManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new FilterByStatusCommand(collectionManager),
                new FilterStartsWithNameCommand(collectionManager),
                new HeadCommand(collectionManager),
                new HelpCommand(commandManager),
                new InfoCommand(collectionManager),
                new LoginCommand(databaseManager),
                new RegisterCommand(databaseManager),
                new RemoveByIDCommand(collectionManager, databaseManager),
                new RemoveGreaterCommand(collectionManager, databaseManager),
                new RemoveLowerCommand(collectionManager, databaseManager),
                new ShowCommand(collectionManager),
                new UpdateCommand(collectionManager, databaseManager)
        ));

        Server server = new Server(port, /*connection_timeout,*/ commandManager, databaseManager, databaseHandler);
        server.runServer();
    }
}
