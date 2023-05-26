package managers;

import console.Print;
import work.Request;
import work.Response;
import work.ResponseStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private Print console;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, Print console) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.console = console;
    }

    /**
     * Подключение к серверу
     */
    public void connectToServer() {
        try {
            if (reconnectionAttempts > 0)
                console.println("Повторное соединение с сервером...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            console.println("Вы подключены к серверу. Ожидайте разрешение на обмен данными...");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            console.println("Разрешение на обмен данными получено.");
        } catch (IllegalArgumentException exception) {
            console.printError("Адрес сервера введен некорректно.");
        } catch (IOException exception) {
            console.printError("Произошла ошибка при соединении с сервером.");
        }
    }

    public Response sendAndAskResponse(Request request) throws IOException {
        while (true) {
            try {
                if(Objects.isNull(serverWriter) || Objects.isNull(serverReader)) throw new IOException();
                if (request.isEmpty()) return new Response(ResponseStatus.WRONG_ARGUMENTS, "Запрос пустой!");
                serverWriter.writeObject(request);
                serverWriter.flush();
                Response response = (Response) serverReader.readObject();
                reconnectionAttempts = 0;
                return response;
            } catch (IOException exception) {
                if (reconnectionAttempts == 0) {
                    connectToServer();
                    reconnectionAttempts++;
                    continue;
                } else console.printError("Соединение с сервером разорвано.");
                try {
                    reconnectionAttempts++;
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        console.printError("Превышено максимальное количество попыток соединения с сервером");
                        return new Response(ResponseStatus.EXIT);
                    }
                    console.println("Повторная попытка...");
                    Thread.sleep(reconnectionTimeout);
                    connectToServer();
                } catch (InterruptedException e) {
                    console.printError("Неуспешное соединение с сервером.");
                }
            } catch (ClassNotFoundException exception) {
                console.printError("Неизвестная ошибка.");
            }
            if (socketChannel != null) socketChannel.close();
            console.println("Работа клиента успешно завершена.");
        }
    }
}
