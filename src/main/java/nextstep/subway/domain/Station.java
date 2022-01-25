package nextstep.subway.domain;

import nextstep.subway.exception.station.StationBlankNameException;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected Station() {
    }

    public Station(String name) {
        validateBlankName(name);
        this.name = name;
    }

    private void validateBlankName(final String name) {
        if (Strings.isBlank(name)) {
            throw new StationBlankNameException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
