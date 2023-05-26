package models;

import java.io.Serializable;

/**
 * Варианты национальности для человека
 */

public enum Country implements Serializable {
    GERMANY,
    VATICAN,
    ITALY,
    THAILAND,
    JAPAN;

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
