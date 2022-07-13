package nextstep.subway.domain.station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Station {
    private static final int MINIMUM_NAME_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected Station() {/*no-op*/}

    public Station(Long id, String name) {

        if (name == null || name.isBlank() || name.length() < MINIMUM_NAME_SIZE) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
