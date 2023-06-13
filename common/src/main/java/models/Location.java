package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс локации для человека
 */

public class Location implements Validator, Serializable {
    private Integer x; // Поле не может быть null
    private Float y; // Поле не может быть null
    private String name; // Строка не может быть пустой

    public Location(Integer x, Float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Проверка корректности полей
     *
     * @return true, если все верно
     */
    @Override
    public boolean validate() {
        return (!(this.x == null) && !(this.y == null) && !this.name.isBlank());
    }

    @Override
    public String toString() {
        return "{" + "\n" +
                "x = " + x + "\n" +
                "y = " + y + "\n" +
                "name = " + name + "\n" + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Location location = (Location) object;
        return x.equals(location.x) && y.equals(location.y) && Objects.equals(name, location.name);
    }
}
