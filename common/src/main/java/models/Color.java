package models;

import java.io.Serializable;

/**
 * Варианты цвета глаз для человека
 */

public enum Color implements Serializable {
    GREEN,
    BLACK,
    YELLOW,
    ORANGE;

    /**
     * @return перечисление в строке всех элементов
     */

    public static String names() {
        StringBuilder list = new StringBuilder();
        for (var forms : values()) {
            list.append(forms.name()).append("\n");
        }
        return list.substring(0, list.length() - 1);
    }
}
