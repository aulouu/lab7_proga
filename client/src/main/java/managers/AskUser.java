package managers;

import console.EmptyConsole;
import console.Print;
import forms.Forms;
import models.Person;
import work.User;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class AskUser extends Forms<User> {
    private final Print console;
    private final Scanner scanner = ScannerManager.getScanner();

    public AskUser(Print console) {
        this.console = (ScannerManager.isFileMode())
                ? new EmptyConsole()
                : console;
    }

    /**
     * Сконструирует объект класса {@link User}
     *
     * @return объект {@link User}
     */
    @Override
    public User build() {
        return new User(askLogin(), askPassword());
    }

    public boolean askAuth() {
        String answer;
        for (; ; ) {
            console.println("У вас есть учетная запись? (+/-)");
            answer = scanner.nextLine().trim();
            if (answer.equals("+")) return true;
            else if (answer.equals("-")) return false;
            else console.printError("Ответ должен быть + или -");
        }
    }

    public String askLogin() {
        String login;
        while (true) {
            console.println("Введите логин: ");
            login = scanner.nextLine().trim();
            if (login.isEmpty()) {
                console.printError("Логин не может быть пустым.");
            } else {
                return login;
            }
        }
    }

    public String askPassword() {
        String password;
        while (true) {
            console.println("Введите пароль: ");
            password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                console.printError("Пароль не может быть пустым.");
            } else {
                return password;
            }
        }
    }
}
