package managers;

public class SQLRequests {

    public static final String ADD_USER = """
            INSERT INTO users (login, password) VALUES (?, ?);
            """;

    public static final String GET_USER = """
            SELECT * FROM users WHERE (login = ?);
            """;

    public static final String CREATE_TABLES = """      
            CREATE TABLE users (
                id SERIAL PRIMARY KEY,
                login TEXT,
                password TEXT
            );
            CREATE TYPE color AS ENUM (
                'GREEN',
                'BLACK',
                'YELLOW',
                'ORANGE'
            );
            CREATE TYPE country AS ENUM (
                'GERMANY',
                'VATICAN',
                'ITALY',
                'THAILAND',
                'JAPAN'
            );
            CREATE TYPE positions AS ENUM (
                'HUMAN_RESOURCES',
                'ENGINEER',
                'HEAD_OF_DIVISION',
                'DEVELOPER',
                'MANAGER_OF_CLEANING'
            );
            CREATE TYPE status AS ENUM (
                'HIRED',
                'RECOMMENDED_FOR_PROMOTION',
                'REGULAR',
                'PROBATION'
            );
            CREATE TABLE IF NOT EXISTS worker (
                id SERIAL PRIMARY KEY,
                name TEXT NOT NULL,
                coordinate_x BIGINT,
                coordinate_y BIGINT CHECK (coordinate_y < 776),
                creation_date TIMESTAMP NOT NULL DEFAULT now(),
                salary BIGINT CHECK (salary > 0),
                start_date DATE NOT NULL,
                position positions NOT NULL,
                status status NOT NULL,
                person_height BIGINT NOT NULL CHECK (person_height > 0),
                person_eye_color color NOT NULL,
                person_nationality country NOT NULL,
                person_location_x BIGINT NOT NULL,
                person_location_y BIGINT NOT NULL,
                person_location_name TEXT,
                owner TEXT NOT NULL
            );
            """;

    public static final String ADD_OBJECT = """
            INSERT INTO worker (name, coordinate_x, coordinate_y, creation_date, salary, start_date, position, status, person_height, person_eye_color, person_nationality, person_location_x, person_location_y, person_location_name, owner)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id;
            """;

    public static final String GET_OBJECTS = """
            SELECT * FROM worker;
            """;

    public static final String UPDATE_OBJECT = """
            UPDATE worker
            SET (name, coordinate_x, coordinate_y, creation_date, salary, start_date, position, status, person_height, person_eye_color, person_nationality, person_location_x, person_location_y, person_location_name)
            = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            WHERE (id = ?)
            RETURNING id;
            """;

    public static final String DELETE_OBJECT = """
            DELETE FROM worker
            WHERE (id = ?) AND (owner = ?)
            RETURNING id;
            """;

}
