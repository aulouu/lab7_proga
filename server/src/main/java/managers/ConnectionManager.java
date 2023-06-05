package managers;

import console.Console;
import console.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

public class ConnectionManager implements Runnable {
    private Server server;
    private Socket clientSocket;
    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private Print console;
    static final Logger connectionManagerLogger = LoggerFactory.getLogger(ConnectionManager.class);

    public ConnectionManager(Server server, Socket clientSocket, CommandManager commandManager, DatabaseManager databaseManager) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
        this.databaseManager = databaseManager;
        this.console = new Console();
    }

    @Override
    public void run() {

    }
}
