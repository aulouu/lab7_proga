package models;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.Objects;

/**
 * Класс для рабочего
 */

public class Worker implements Validator, Comparable<Worker>, Serializable {
    private int id; //Значение поля должно быть больше 0, быть уникальным, генерироваться автоматически
    private String name; // Поле не может быть null, строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private LocalDateTime creationDate; // Поле не может быть null, значение должно генерироваться автоматически
    private long salary; // Значение поля должно быть больше 0
    private LocalDate startDate; // Поле не может быть null
    private Position position; // Поле не может быть null
    private Status status; // Поле может быть null
    private Person person; // Поле не может быть null
    private String user;

    private static int nextID = 0;

    public Worker(String name, Coordinates coordinates, LocalDateTime creationDate, long salary, LocalDate startDate, Position position, Status status, Person person) {
        this.id = incNextId();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.startDate = startDate;
        this.position = position;
        this.status = status;
        this.person = person;
    }

    public Worker(int id, String name, Coordinates coordinates, LocalDateTime creationDate, long salary, LocalDate startDate, Position position, Status status, Person person, String user) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.startDate = startDate;
        this.position = position;
        this.status = status;
        this.person = person;
        this.user = user;
    }

    /**
     * Увеличивает ID
     *
     * @return следующий ID, чтобы они не повторялись
     */
    public static int incNextId() {
        return nextID++;
    }

    /**
     * Обновляет ID для следующего элемента
     *
     * @param collection коллекция, в которой получаем ID
     */

    public static void updateId(ArrayDeque<Worker> collection) {
        int maxId = 0;
        for (Worker worker : collection) {
            if (Objects.isNull(worker)) continue;
            maxId = Math.max(maxId, worker.getId());
        }
        nextID = maxId + 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Компаратор объектов, сравнивающий по зарплате
     *
     * @param worker the object to be compared.
     */
    @Override
    public int compareTo(Worker worker) {
        int result = (int) (this.getSalary() - worker.getSalary());
        if (result == 0)
            result = this.getId() - worker.getId();
        return result;
    }

    /**
     * Проверка корректности полей
     *
     * @return true, если все верно
     */
    @Override
    public boolean validate() {
        if (this.id < 0) return false;
        if (this.name == null || this.name.isBlank()) return false;
        if (this.coordinates == null) return false;
        if (this.creationDate == null) return false;
        if (this.salary <= 0) return false;
        if (this.startDate == null) return false;
        if (!person.validate()) return false;
        return this.position != null;
    }

    @Override
    public String toString() {
        return "Worker: " + "\n" +
                "id = " + id + "\n" +
                "name = " + name + "\n" +
                "coordinates = " + coordinates + "\n" +
                "creationDate = " + creationDate + "\n" +
                "salary = " + salary + "\n" +
                "startDate = " + startDate + "\n" +
                "position = " + position + "\n" +
                "status = " + status + "\n" +
                "person = " + person + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, salary, startDate, position, status, person);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Worker that = (Worker) object;
        if (id != that.id) return false;
        if (salary != that.salary) return false;
        if (!name.equals(that.name)) return false;
        if (!coordinates.equals(that.coordinates)) return false;
        if (!creationDate.equals(that.creationDate)) return false;
        if (!startDate.equals(that.startDate)) return false;
        if (!position.equals(that.position)) return false;
        if (!status.equals(that.status)) return false;
        return Objects.equals(person, that.person);
    }
}
