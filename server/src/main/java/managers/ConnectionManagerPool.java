package managers;

import work.Response;

import java.io.ObjectOutputStream;

public record ConnectionManagerPool(Response response, ObjectOutputStream objectOutputStream){};
