package forms;

import console.EmptyConsole;
import console.Print;
import managers.ScannerManager;
import models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class WorkerForm extends Forms<Worker> {
    private final Print console;
    private final Scanner scanner = ScannerManager.getScanner();

    public WorkerForm(Print console) {
        this.console = (ScannerManager.isFileMode())
                ? new EmptyConsole()
                : console;
    }

    /**
     * Сконструирует объект класса {@link Worker}
     *
     * @return объект {@link Worker}
     */
    @Override
    public Worker build() {
        return new Worker(askName(), askCoordinates(), LocalDateTime.now(), askSalary(), askStartDate(), askPosition(), askStatus(), askPerson());
    }

    public String askName() {
        String name;
        while (true) {
            console.println("Введите имя:");
            name = scanner.nextLine().trim();
            if (name.isBlank()) {
                console.printError("Имя не может быть пустым.");
                if (ScannerManager.isFileMode()) return "";
            } else {
                return name;
            }
        }
    }

    public Coordinates askCoordinates() {
        return new CoordinatesForm(console).build();
    }

    public long askSalary() {
        while (true) {
            console.println("Введите зарплату: ");
            String input = scanner.nextLine().trim();
            try {
                if (Long.parseLong(input) <= 0) {
                    console.printError("Зарплата должна быть больше 0.");
                    if (ScannerManager.isFileMode()) return -1;
                } else {
                    return Long.parseLong(input);
                }
            } catch (NumberFormatException exception) {
                console.printError("Зарплата должна быть числом типа long");
            } catch (Throwable throwable) {
                console.printError("Непредвиденная ошибка!");
            }
        }
    }

    public LocalDate askStartDate() {
        while (true) {
            console.println("Введите дату начала работы в формате 2011-12-03 : ");
            String input = scanner.nextLine().trim();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException exception) {
                console.printError("Неверный формат.");
            }
        }
    }

    public Position askPosition() {
        return new PositionForm(console).build();
    }

    public Status askStatus() {
        return new StatusForm(console).build();
    }

    public Person askPerson() {
        return new PersonForm(console).build();
    }
}
