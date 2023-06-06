import console.Console;
import console.Print;
import exceptions.IllegalArgument;
import managers.*;

import java.util.Scanner;

public class App {
    private static Print console = new Console();
    private static String host;
    private static int port;

    private static boolean initializeConnectionAddress(String[] args) {
        try {
            if (args.length != 2)
                throw new IllegalArgument("В аргументы командной строки необходимо передать host port.");
            host = args[0];
            port = Integer.parseInt(args[1]);
            if (port < 0) throw new IllegalArgument("Порт не может быть отрицательным.");
            return true;
        } catch (IllegalArgument exception) {
            console.printError(exception.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        if (!initializeConnectionAddress(args)) return;
        Client client = new Client(host, port, 10000, 5, console);
        new RuntimeManager(console, new Scanner(System.in), client).runTime();
    }
}
