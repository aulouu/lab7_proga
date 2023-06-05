package work;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User: " + "\n" +
                "name = " + login + "\n" +
                "password = ********" + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object instanceof User) {
            User userObject = (User) object;
            return login.equals(userObject.getLogin()) && password.equals(userObject.getPassword());
        }
        return false;
    }
}
