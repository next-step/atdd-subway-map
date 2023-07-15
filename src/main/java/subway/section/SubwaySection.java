package subway.section;

import subway.station.Station;

import javax.persistence.*;

@Entity
public class SubwaySection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    public SubwaySection(Station upStation, Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Long getId() {
        return id;
    }
}