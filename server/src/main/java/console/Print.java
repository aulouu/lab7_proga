package console;

/**
 * Интерфейс, который реализовывает способы вывода
 */

public interface Print {
    void println(String str);

    void print(String str);

    void printError(String str);
}
