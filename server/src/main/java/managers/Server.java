package managers;

import console.Console;
import console.Print;
import exceptions.ClosingSocket;
import exceptions.ConnectionDatabaseError;
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
    //private int soTimeout;
    private volatile boolean isStopped;
    private ServerSocket serverSocket;
    private final CommandManager commandManager;
    private final DatabaseManager databaseManager;
    private final DatabaseHandler databaseHandler;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
    static final Logger serverLogger = LoggerFactory.getLogger(Server.class);

    public Server(int port, /*int soTimeout,*/ CommandManager commandManager, DatabaseManager databaseManager, DatabaseHandler databaseHandler) {
        this.port = port;
        //this.soTimeout = soTimeout;
        this.commandManager = commandManager;
        this.databaseManager = databaseManager;
        this.databaseHandler = databaseHandler;
    }

    /**
     * Открывает серверный сокет
     */
    private void open() throws OpeningServer {
        try {
            if (!databaseHandler.isConnect) throw new ConnectionDatabaseError();
            serverLogger.info("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            //serverSocket.setSoTimeout(soTimeout);
            serverLogger.info("Сервер запущен успешно.");
        } catch (ConnectionDatabaseError exception) {
            serverLogger.error("База данных не подключена, попробуйте еще раз.");
            System.exit(1);
        } catch (IllegalArgumentException exception) {
            serverLogger.error("Порт " + port + "недоступен.");
            throw new OpeningServer();
        } catch (IOException exception) {
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
            serverLogger.info("Соединение с клиентом установлено успешно.");
            return clientSocket;
        } catch (IOException exception) {
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
            fixedThreadPool.submit(() -> {
                while (!isStopped()) {
                    try {
                        if (isStopped()) throw new ConnectionError();
                        Socket clientSocket = connectToClient();
                        new Thread(new ConnectionManager(this, clientSocket, commandManager, databaseManager)).start();
                    } catch (SocketTimeoutException ignored) {
                    } catch (ConnectionError exception) {
                        if (!isStopped) {
                            serverLogger.error("Произошла ошибка при соединении с клиентом.");
                        }
                    }
                }
                stop();
            });
        } catch (OpeningServer exception) {
            serverLogger.error("Сервер не может быть запущен.");
        }
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
            serverLogger.info("Работа сервера успешно завершена.");
        } catch (ClosingSocket exception) {
            serverLogger.error("Нельзя завершить работу незапущенного сервера.");
        } catch (IOException exception) {
            serverLogger.error("Произошла ошибка при завершении работы сервера.");
        }
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }
}
