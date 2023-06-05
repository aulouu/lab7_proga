package managers;

import console.Console;
import console.Print;
import exceptions.ClosingSocket;
import exceptions.ConnectionError;
import exceptions.OpeningServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private int soTimeout;
    private Print console;
    private boolean isStopped;
    private ServerSocket serverSocket;
    private final CommandManager commandManager;
    private final DatabaseManager databaseManager;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    static final Logger serverLogger = LoggerFactory.getLogger(Server.class);

    public Server(int port, int soTimeout, CommandManager commandManager, DatabaseManager databaseManager) {
        this.port = port;
        this.soTimeout = soTimeout;
        this.commandManager = commandManager;
        this.databaseManager = databaseManager;
        this.console = new Console();
    }

    /**
     * Открывает серверный сокет
     */
    private void open() throws OpeningServer {
        try {
            serverLogger.info("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
            serverLogger.info("Сервер запущен успешно.");
        } catch (IllegalArgumentException exception) {
            console.printError("Порт " + port + "недоступен.");
            serverLogger.error("Порт " + port + "недоступен.");
            throw new OpeningServer();
        } catch (IOException exception) {
            console.printError("Ошибка при использовании порта " + port);
            serverLogger.error("Ошибка при использовании порта " + port);
            throw new OpeningServer();
        }
    }

    /**
     * Подключение к клиенту
     */
    private Socket connectToClient() throws ConnectionError, SocketTimeoutException {
        try {
            Socket clientSocket = serverSocket.accept();
            console.println("Соединение с клиентом установлено успешно.");
            serverLogger.info("Соединение с клиентом установлено успешно.");
            return clientSocket;
        } catch (IOException exception) {
            console.printError("Произошла ошибка при соединении с клиентом.");
            serverLogger.error("Произошла ошибка при соединении с клиентом.");
            throw new ConnectionError();
        }
    }

    /**
     * Начало работы сервера
     */
    public void runServer() {
        try {
            open();
            while (!isStopped()) {
                try  {
                    if (isStopped()) throw new ConnectionError();
                    Socket clientSocket = connectToClient();
                    fixedThreadPool.submit(new ConnectionManager(this, clientSocket, commandManager, databaseManager));
                } catch (SocketTimeoutException ignored) {
                } catch (ConnectionError exception) {
                    if (!isStopped) {
                        console.printError("Произошла ошибка при соединении с клиентом.");
                        serverLogger.error("Произошла ошибка при соединении с клиентом.");
                    } else break;
                }
            }
        } catch (OpeningServer exception) {
            console.printError("Сервер не может быть запущен.");
            serverLogger.error("Сервер не может быть запущен.");
        }
        stop();
    }

    /**
     * Заканчивает работу сервера
     */
    public synchronized void stop() {
        try {
            serverLogger.info("Завершение работы сервера...");
            if (serverSocket == null) throw new ClosingSocket();
            isStopped = true;
            fixedThreadPool.shutdown();
            serverSocket.close();
            console.println("Работа сервера успешно завершена.");
            serverLogger.info("Работа сервера успешно завершена.");
        } catch (ClosingSocket exception) {
            console.printError("Нельзя завершить работу незапущенного сервера.");
            serverLogger.error("Нельзя завершить работу незапущенного сервера.");
        } catch (IOException exception) {
            console.printError("Произошла ошибка при завершении работы сервера.");
            serverLogger.error("Произошла ошибка при завершении работы сервера.");
        }
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }
}
