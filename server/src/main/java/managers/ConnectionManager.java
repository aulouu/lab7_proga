package managers;

import console.Console;
import console.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.io.*;
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
        Request userRequest = null;
        Response responseToUser = null;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = new RequestHandler(userRequest, commandManager).handle(userRequest);
                Response finalResponseToUser = responseToUser;
                //serverLogger.info("Запрос обработан успешно." /*+ userRequest.getCommandName() + " обработан успешно."*/);
                new Thread(() -> {
                    try {
                        clientWriter.writeObject(finalResponseToUser);
                        clientWriter.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } ).start();
            } while (responseToUser.getResponseStatus() != ResponseStatus.EXIT);
            clientSocket.close();
        } catch (ClassNotFoundException exception) {
            console.printError("Произошла ошибка при чтении данных.");
            connectionManagerLogger.error("Произошла ошибка при чтении данных.");
        } catch (InvalidClassException | NotSerializableException exception) {
            console.printError("Произошла ошибка при отправке данных.");
            connectionManagerLogger.error("Произошла ошибка при отправке данных.");
        } catch (IOException exception) {
            if (userRequest == null) {
                console.printError("Разрыв соединения с клиентом.");
                connectionManagerLogger.error("Разрыв соединения с клиентом.");
            } else {
                connectionManagerLogger.info("Клиент отключен от сервера успешно.");
            }
        }
    }
}
