package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс для координат
 */

public class Coordinates implements Validator, Serializable {
    private long x;
    private float y; // Максимальное значение поля: 776

    public Coordinates(long x, float y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * Проверка корректности полей
     *
     * @return true, если все верно
     */
    @Override
    public boolean validate() {
        return (this.y > 776);
    }

    @Override
    public String toString() {
        return "{x = " + this.x + ", y = " + this.y + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Coordinates that = (Coordinates) object;
        if (y != that.y) return false;
        return x == that.x;
    }
}
