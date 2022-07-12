package nextstep.subway.domain.section;

import nextstep.subway.domain.station.Station;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn
    private Station upStation;

    @OneToOne
    @JoinColumn
    private Station downStation;

    private Long distance;

    public Section() {

    }

    public Section(String name, String color, Station upStation, Station downStation, Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

}
