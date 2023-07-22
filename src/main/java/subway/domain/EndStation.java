package subway.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class EndStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Station station;

    @Enumerated(EnumType.STRING)
    private DirectionType directionType;

    public EndStation() {}

    public EndStation(Station station, DirectionType directionType) {
        this.station = station;
        this.directionType = directionType;
    }

    public Long getId() {
        return id;
    }

    public DirectionType getDirectionType() {
        return directionType;
    }

    public Station getStation() {
        return station;
    }
}
