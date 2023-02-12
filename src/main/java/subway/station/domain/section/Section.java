package subway.station.domain.section;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.line.Line;
import subway.station.domain.station.Station;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
public class Section {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SECTION_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "STATION_ID")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "STATION_ID")
    private Station downStation;
    private Long distance;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "LINE_ID")
    private Line line;

    public Section() {
    }

    @Builder
    public Section(Station upStation, Station downStation, Long distance,Line line) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(getId(), section.getId()) && Objects.equals(getUpStation(), section.getUpStation()) && Objects.equals(getDownStation(), section.getDownStation()) && Objects.equals(getDistance(), section.getDistance()) && Objects.equals(getLine(), section.getLine());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUpStation(), getDownStation(), getDistance(), getLine());
    }
}
