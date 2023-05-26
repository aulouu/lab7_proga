package console;

/**
 * Класс для вывода в пустую консоль
 */

public class EmptyConsole implements Print {
    @Override
    public void println(String str) {

    }

    @Override
    public void print(String str) {

    }

    @Override
    public void printError(String str) {

    }
}
