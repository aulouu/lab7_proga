package commands;

import java.util.Objects;

/**
 * Команда для взаимодействия пользователя с программой
 */

public abstract class Command implements CommandExecute {
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return имя команды
     */
    public String getName() {
        return name;
    }

    /**
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " : " + description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Command abstractCommand = (Command) object;
        return Objects.equals(name, abstractCommand.name) && Objects.equals(description, abstractCommand.description);
    }
}
