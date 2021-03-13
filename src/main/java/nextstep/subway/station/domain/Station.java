package nextstep.subway.station.domain;

import nextstep.subway.common.BaseEntity;

import javax.persistence.*;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    StationStatus status;

    public Station() {
    }

    public Station(String name) {
        this.status = StationStatus.WAIT;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StationStatus getStatus() {
        return status;
    }

    public void setStatus(StationStatus status) {
        this.status = status;
    }
}
