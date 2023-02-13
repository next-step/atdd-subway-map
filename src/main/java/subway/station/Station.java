package subway.station;

import subway.station.presentation.StationResponse;

import javax.persistence.*;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(StationResponse stationResponse) {
        this.id = stationResponse.getId();
        this.name = stationResponse.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
