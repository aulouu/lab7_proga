package models;

import java.io.Serializable;

/**
 * Варианты статуса
 */

public enum Status implements Serializable {
    HIRED,
    RECOMMENDED_FOR_PROMOTION,
    REGULAR,
    PROBATION;

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
