package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс человека
 */

public class Person implements Validator, Serializable {
    private Integer height; // Поле не может быть null, значение поля должно быть больше 0
    private Color eyeColor; // Поле не может быть null
    private Country nationality; // Поле не может быть null
    private Location location; // Поле не может быть null

    public Person(Integer height, Color eyeColor, Country nationality, Location location) {
        this.height = height;
        this.eyeColor = eyeColor;
        this.nationality = nationality;
        this.location = location;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Проверка корректности полей
     *
     * @return true, если все верно
     */
    @Override
    public boolean validate() {
        if (this.height == null || this.height < 0) return false;
        if (!location.validate()) return false;
        return (!(this.eyeColor == null) && !(this.nationality == null) && !(this.location == null));
    }

    @Override
    public String toString() {
        return "[ " + "\n" +
                "height = " + height + "\n" +
                "eyeColor = " + eyeColor + "\n" +
                "nationality = " + nationality + "\n" +
                "location = " + location + "\n" + " ]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, eyeColor, nationality, location);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Person person = (Person) object;
        if (!Objects.equals(height, person.height)) return false;
        if (eyeColor != person.eyeColor) return false;
        if (nationality != person.nationality) return false;
        return Objects.equals(location, person.location);
    }
}
