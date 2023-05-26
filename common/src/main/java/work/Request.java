package work;

import models.Worker;

import java.io.Serializable;
import java.util.Objects;

public class Request implements Serializable {
    private String commandName;
    private String args = "";
    private Worker object = null;

    public String getCommandName() {
        return commandName;
    }

    public String getArgs() {
        return args;
    }

    public Worker getObject() {
        return object;
    }

    public Request(ResponseStatus OK, String commandName, Worker help) {
        this.commandName = commandName.trim();
    }

    public Request(String commandName, String args) {
        this.commandName = commandName.trim();
        this.args = args;
    }

    public Request(String commandName, Worker object) {
        this.commandName = commandName.trim();
        this.object = object;
    }

    public Request(String commandName, String args, Worker object) {
        this.commandName = commandName.trim();
        this.args = args.trim();
        this.object = object;
    }

    public boolean isEmpty() {
        return commandName.isEmpty() && args.isEmpty() && object == null;
    }

    @Override
    public String toString() {
        return "Request: " + "\n" +
                "commandName = " + commandName + "\n" +
                "args = " + (args.isEmpty()
                ? ""
                : args) + "\n" +
                "object = " + ((object == null)
                ? ""
                : object) + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandName, args, object);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Request request)) return false;
        return Objects.equals(commandName, request.commandName) && Objects.equals(args, request.args) && Objects.equals(object, request.object);
    }
}
