package subway.section.entity;

import lombok.*;
import subway.line.entity.Line;
import subway.station.entity.Station;

import javax.persistence.*;


@ToString
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @JoinColumn(name = "line_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Line line;

    @JoinColumn(name = "down_station_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Station downStation;

    @JoinColumn(name = "up_station_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Station upStation;

    @Column(length = 20, nullable = false)
    private long distance;

    @Builder
    public Section(Station downStation, Station upStation, long distance) {
        this.downStation = downStation;
        this.upStation = upStation;
        this.distance = distance;
    }

    public void changeLine(Line line) {
        if (this.line != null) {
            this.line.remove(this);
        }

        line.addSection(this);
        this.line = line;
    }
}
