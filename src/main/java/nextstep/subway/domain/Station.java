package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Station {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id")
    private Section section;
    public void update(Station station) {
        if (station == null) {
            return;
        }

        this.name = station.getName() != null ? station.getName() : this.name;
    }

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setSection(Section section) {
        this.section = section;
    }

}
