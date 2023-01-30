package subway.section.entity;

import lombok.*;
import subway.line.entity.Line;
import subway.station.entity.Station;

import javax.persistence.*;


@Builder
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    @JoinColumn(name = "down_station_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Station downStation;

    @JoinColumn(name = "up_station_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @Column(length = 20, nullable = false)
    private long distance;

    public void changeLine(Line newLine) {
        if (line != null && line.equals(newLine)) {
            return;
        }

        if (line != null) {
            line.remove(this);
        }

        newLine.addSection(this);
        line = newLine;
    }

    public Long upStationId() {
        return upStation.getId();
    }

    public Long downStationId() {
        return downStation.getId();
    }

    public void removeLine() {
        if (line == null) {
            return;
        }

        line.remove(this);
        line = null;
    }
}
