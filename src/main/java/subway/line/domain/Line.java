package subway.line.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String color;

    @OneToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Column(nullable = false)
    private Long distance;

    @Builder
    private Line(final Long id, final String name, final String color,
                 final Station upStation, final Station downStation, final Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void updateLine(final Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }
}
