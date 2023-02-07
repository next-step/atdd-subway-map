package subway.line.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Section {

    @Id
    @GeneratedValue
    @Column(name = "section_id")
    private Long id;

    @Column(nullable = false)
    private Long distance;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @Builder
    private Section(final Long id, final Station upStation, final Station downStation,
                    final Long distance, final Line line) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.line = line;
    }

    public static Section createSection(final Line line, final Station upStation,
                                        final Station downStation, final Long distance) {
        return Section.builder()
                .distance(distance)
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .build();
    }
}
