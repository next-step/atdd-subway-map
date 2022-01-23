package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationRequest;

import javax.persistence.*;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public static Station of(String name) {
        return new Station(name);
    }

    public static Station of(StationRequest stationRequest) {
        return new Station(stationRequest.getName());
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
