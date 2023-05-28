package work;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String name;
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User: " + "\n" +
                "name = " + name + "\n" +
                "password = ********" + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object instanceof User) {
            User userObject = (User) object;
            return name.equals(userObject.getName()) && password.equals(userObject.getPassword());
        }
        return false;
    }
}
