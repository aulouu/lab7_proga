import commands.*;
import console.Console;
import console.Print;
import exceptions.IllegalArgument;
import managers.*;

import java.util.List;

public class App {
    private static Print console = new Console();
    public static int port = 6090;
    //public static final int connection_timeout = 60 * 1000;
    //private static final Print console = new Console();

    public static void main(String[] args) {
        /*try {
            if (args.length != 1)
                throw new IllegalArgument("В аргументы командной строки необходимо передать port.");
            port = Integer.parseInt(args[0]);
            if (port < 0) throw new IllegalArgument("Порт не может быть отрицательным.");
        } catch (IllegalArgument exception) {
            console.printError(exception.getMessage());
            System.exit(1);
        }*/
        if (args.length != 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }
        DatabaseHandler databaseHandler = new DatabaseHandler();
        DatabaseManager databaseManager = new DatabaseManager(databaseHandler);
        CollectionManager collectionManager = new CollectionManager(databaseManager, databaseHandler);
        CommandManager commandManager = new CommandManager(databaseManager, databaseHandler);

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
