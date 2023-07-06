package subway.line.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.global.common.BaseEntity;
import subway.station.domain.Station;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Embedded
    private LineSections sections;

    public Line(final String name, final String color, final Station upStation, final Station downStation,
                final Long distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.sections = LineSections.init(new Section(this, upStation, downStation, distance));
    }

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void appendSection(final Section section) {
        sections.append(section);
        upStation = sections.getFirstStation();
    }

    public void removeSection(final Station station) {
        sections.remove(station);
        downStation = sections.getLastStation();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
