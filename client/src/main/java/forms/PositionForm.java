package forms;

import console.EmptyConsole;
import console.Print;
import managers.ScannerManager;
import models.Position;

import java.util.Scanner;

public class PositionForm extends Forms<Position> {
    private final Print console;
    private final Scanner scanner = ScannerManager.getScanner();

    public PositionForm(Print console) {
        this.console = (ScannerManager.isFileMode())
                ? new EmptyConsole()
                : console;
    }

    @Override
    public Position build() {
        console.println("Варианты позиции: ");
        console.println(Position.names());
        while (true) {
            console.println("Введите позицию: ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return Position.valueOf(input);
            } catch (IllegalArgumentException exception) {
                console.printError("Такая позиция не доступна.");
                if (ScannerManager.isFileMode()) return null;
            } catch (Throwable throwable) {
                console.printError("Непредвиденная ошибка!");
            }
        }
    }
}
