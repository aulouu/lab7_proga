package work;

import models.Worker;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class Response implements Serializable {
    private final ResponseStatus responseStatus;
    private String response = "";
    private Collection<Worker> collection;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public String getResponse() {
        return response;
    }

    public Collection<Worker> getCollection() {
        return collection;
    }

    public Response(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Response(ResponseStatus responseStatus, String response) {
        this.responseStatus = responseStatus;
        this.response = response.trim();
    }

    public Response(ResponseStatus responseStatus, String response, Collection<Worker> collection) {
        this.responseStatus = responseStatus;
        this.response = response.trim();
        this.collection = collection.stream()
                .sorted(Comparator.comparing(Worker::getId))
                .toList();
    }

    @Override
    public String toString() {
        return "Response: " + "\n" +
                "status = " + responseStatus + "\n" +
                "response = " + response + "\n" +
                "collection = " + collection + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseStatus, response, collection);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Response res = (Response) object;

        if (responseStatus != res.responseStatus) return false;
        if (!Objects.equals(response, res.response)) return false;
        return Objects.equals(collection, res.collection);
    }
}
