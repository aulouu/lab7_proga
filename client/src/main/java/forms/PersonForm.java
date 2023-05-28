package forms;

import console.EmptyConsole;
import console.Print;
import managers.ScannerManager;
import models.*;

import java.util.Scanner;

public class PersonForm extends Forms<Person> {
    private final Print console;
    private final Scanner scanner = ScannerManager.getScanner();

    public PersonForm(Print console) {
        this.console = (ScannerManager.isFileMode())
                ? new EmptyConsole()
                : console;
    }

    /**
     * Сконструирует объект класса {@link Person}
     *
     * @return объект {@link Person}
     */
    @Override
    public Person build() {
        return new Person(askHeight(), askColor(), askCountry(), askLocation());
    }

    public Integer askHeight() {
        while (true) {
            console.println("Введите рост: ");
            String input = scanner.nextLine().trim();
            try {
                if (Integer.parseInt(input) <= 0) {
                    console.printError("Рост должен быть больше 0.");
                    if (ScannerManager.isFileMode()) return -1;
                } else {
                    return Integer.parseInt(input);
                }
            } catch (NumberFormatException exception) {
                console.printError("Рост должен быть числом типа integer");
            } catch (Throwable throwable) {
                console.printError("Непредвиденная ошибка!");
            }
        }
    }

    public Color askColor() {
        return new ColorForm(console).build();
    }

    public Country askCountry() {
        return new CountryForm(console).build();
    }

    public Location askLocation() {
        return new LocationForm(console).build();
    }
}
