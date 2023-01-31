package subway.station.repository.entity;

import subway.station.business.model.Station;

import javax.persistence.*;

@Entity
public class StationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    public StationEntity() {
    }

    public StationEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Station toStation() {
        return Station.builder()
                .id(id)
                .name(name)
                .build();
    }

}
