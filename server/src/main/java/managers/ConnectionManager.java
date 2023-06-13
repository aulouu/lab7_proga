package managers;

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
    static final Logger connectionManagerLogger = LoggerFactory.getLogger(ConnectionManager.class);

    public ConnectionManager(Server server, Socket clientSocket, CommandManager commandManager, DatabaseManager databaseManager) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
        this.databaseManager = databaseManager;
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
                //connectionManagerLogger.info("Запрос обработан успешно." /*+ userRequest.getCommandName() + " обработан успешно."*/);
                new Thread(() -> {
                    try {
                        clientWriter.writeObject(finalResponseToUser);
                        clientWriter.flush();
                    } catch (IOException exception) {
                        connectionManagerLogger.error("Произошла ошибка при отправке данных на клиент.");
                    }
                }).start();
            } while (responseToUser.getResponseStatus() != ResponseStatus.SERVER_EXIT);
            clientSocket.close();
        } catch (ClassNotFoundException exception) {
            connectionManagerLogger.error("Произошла ошибка при чтении данных.");
        } catch (InvalidClassException | NotSerializableException exception) {
            connectionManagerLogger.error("Произошла ошибка при отправке данных.");
        } catch (IOException exception) {
            if (userRequest == null) {
                connectionManagerLogger.error("Разрыв соединения с клиентом.");
            } else {
                connectionManagerLogger.info("Клиент отключен от сервера успешно.");
            }
        }
    }
}
