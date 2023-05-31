package managers;

import console.Console;
import console.Print;
import exceptions.InvalidForm;
import models.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Менеджер коллекции - класс для работы с коллекцией
 */

public class CollectionManager {
    private Deque<Worker> collection = new ConcurrentLinkedDeque<>();
    /**
     * Дата создания коллекции
     */
    private LocalDateTime lastInitTime;
    private DatabaseManager databaseManager;
    private Print console;
    static final Logger collectionManagerLogger = LoggerFactory.getLogger(CollectionManager.class);

    public CollectionManager(DatabaseManager databaseManager) {
        this.lastInitTime = null;
        this.databaseManager = databaseManager;
        this.console = new Console();

        loadCollection();
    }

    /**
     * @return коллекция
     */
    public Deque<Worker> getCollection() {
        return collection;
    }

    /**
     * @return время последней инициализации
     */
    public String getLastInitTime() {
        return lastInitTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * @return имя типа коллекции
     */
    public String collectionType() {
        return collection.getClass().getName();
    }

    /**
     * @return размер коллекции
     */
    public int collectionSize() {
        return collection.size();
    }

    /**
     * Очищает коллекцию
     */
    public void clear() {
        this.collection.clear();
    }

    /**
     * Загружает коллекцию из файла
     */
    private void loadCollection() {
        try {
            lastInitTime = LocalDateTime.now();
            collection = databaseManager.readAll();
            console.println("Коллекция загружена.");
            collectionManagerLogger.info("Коллекция загружена.");
        } catch (SQLException exception) {
            collection = new ConcurrentLinkedDeque<>();
            console.printError("Коллекция не может быть загружена.");
            collectionManagerLogger.error("Коллекция не может быть загружена.");
        }
    }

    /**
     * Проверяет, существует ли элемент с таким id
     *
     * @param id id
     */
    public boolean checkId(int id) {
        return collection.stream()
                .anyMatch((x) -> x.getId() == id);
    }

    /**
     * @param id id
     * @return элемент с таким id или null, если элемента с таким id нет
     */
    public Worker getById(int id) {
        for (Worker element : collection) {
            if (element.getId() == id) return element;
        }
        return null;
    }

    /**
     * Изменяет элемент по id
     *
     * @param id         id
     * @param newElement новый элемент
     * @throws InvalidForm несуществующий элемент с таким id
     */
    public void editById(int id, Worker newElement) {
        Worker pastElement = this.getById(id);
        this.removeElement(pastElement);
        newElement.setId(id);
        this.addElement(newElement);
        Worker.updateId(this.getCollection());
        collectionManagerLogger.info("Элемент с id = " + id + " изменен.", newElement);
    }

    /**
     * Удаляет элемент из коллекции
     *
     * @param worker элемент
     */
    public void removeElement(Worker worker) {
        collection.remove(worker);
        collectionManagerLogger.info("Элемент удален.");
    }

    /**
     * Удаляет все элементы из коллекции
     *
     * @param collection коллекция
     */
    public void removeElements(Collection<Worker> collection) {
        this.collection.removeAll(collection);
        collectionManagerLogger.info("Все элементы коллекции удалены.");
    }

    /**
     * Добавляет элемент в коллекцию
     *
     * @param worker элемент
     * @throws InvalidForm неправильно заданные поля
     */
    public void addElement(Worker worker) {
        collection.add(worker);
        collectionManagerLogger.info("Элемент добавлен в коллекцию.");
    }

    /**
     * Метод для команды Head
     *
     * @return первый элемент
     */
    public Worker head() {
        collectionManagerLogger.info("Первый элемент коллекции: ");
        return collection.peekFirst();
    }
}
