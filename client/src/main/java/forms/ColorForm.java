package forms;

import console.EmptyConsole;
import console.Print;
import managers.ScannerManager;
import models.Color;

import java.util.Scanner;

/**
 * Форма для цвета глаз
 */

public class ColorForm extends Forms<Color> {

    private final Print console;
    private final Scanner scanner = ScannerManager.getScanner();

    public ColorForm(Print console) {
        this.console = (ScannerManager.isFileMode())
                ? new EmptyConsole()
                : console;
    }

    @Override
    public Color build() {
        console.println("Варианты цвета глаз: ");
        console.println(Color.names());
        while (true) {
            console.println("Введите цвет глаз: ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return Color.valueOf(input);
            } catch (IllegalArgumentException exception) {
                console.printError("Такой цвет глаз не доступен.");
                if (ScannerManager.isFileMode()) return null;
            } catch (Throwable throwable) {
                console.printError("Непредвиденная ошибка!");
            }
        }
    }
}
