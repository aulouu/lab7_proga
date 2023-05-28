package managers;

import java.util.Scanner;

/**
 * Класс, который хранит сканнер для программы и управляет режимами ввода
 */

public class ScannerManager {
    private static Scanner Scanner = new Scanner(System.in);
    private static boolean fileMode = false;

    /**
     * @return сканер
     */
    public static Scanner getScanner() {
        return Scanner;
    }

    /**
     * Устанавливает сканер
     *
     * @param scanner сканер
     */
    public static void setScanner(Scanner scanner) {
        ScannerManager.Scanner = scanner;
    }

    /**
     * @return работа с файлом
     */
    public static boolean isFileMode() {
        return fileMode;
    }

    /**
     * Устанавливает работу с файлом
     */
    public static void setFileMode() {
        ScannerManager.fileMode = true;
    }

    /**
     * Устанавливает работу с консолью
     */
    public static void setUserMode() {
        ScannerManager.fileMode = false;
    }
}
