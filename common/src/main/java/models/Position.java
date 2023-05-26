package models;

import java.io.Serializable;

/**
 * Варианты позиции
 */

public enum Position implements Serializable {
    HUMAN_RESOURCES,
    ENGINEER,
    HEAD_OF_DIVISION,
    DEVELOPER,
    MANAGER_OF_CLEANING;

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
