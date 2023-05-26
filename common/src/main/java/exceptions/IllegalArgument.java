package exceptions;

import java.io.IOException;

/**
 * Исключение для неверных аргументов команд
 */

public class IllegalArgument extends IOException {
    public IllegalArgument() {

    }

    public IllegalArgument(String s) {
        super(s);
    }
}
