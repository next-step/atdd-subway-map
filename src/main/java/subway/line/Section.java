package subway.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.Station;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;
    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    private Integer distance;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "line")
    private Line line;

    public Section(final Station downStation, final Station upStation, final Integer distance) {
        this.downStation = downStation;
        this.upStation = upStation;
        this.distance = distance;
    }

    public void addLine(final Line line) {
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(downStation, section.downStation) && Objects.equals(upStation, section.upStation) && Objects.equals(distance, section.distance) && Objects.equals(line, section.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, downStation, upStation, distance, line);
    }
}
