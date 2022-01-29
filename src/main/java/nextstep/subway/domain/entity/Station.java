package nextstep.subway.domain.entity;

import nextstep.subway.domain.service.Validator;

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

    public Station() {
    }

    public Station(final String name, final Validator<Station> stationValidator) {
        this.name = name;

        stationValidator.validate(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
