package console;

/**
 * Класс для вывода в стандартную консоль
 */

public class Console implements Print {
    @Override
    public void println(String str) {
        System.out.println(str);
    }

    @Override
    public void print(String str) {
        System.out.print(str);
    }

    @Override
    public void printError(String str) {
        System.out.println(str);
    }
}
