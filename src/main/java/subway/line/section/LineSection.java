package subway.line.section;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.Line;
import subway.section.Section;
import subway.station.Station;

@Entity
@Table(name = "LINE_SECTION")
@NoArgsConstructor
@Getter
public class LineSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LINE_ID")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECTION_ID")
    private Section section;

    private int number;

    public LineSection(Line line, Section section) {
        this.line = line;
        this.section = section;
    }

    public void setLine(Line line) {
        if (this.line == line) {
            return;
        }

        if (this.line != null) {
            this.line.removeLineSection(this);
        }

        this.line = line;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Station getDownStation() {
        return section.getDownStation();
    }

    public Station getUpStation() {
        return section.getUpStation();
    }

    public boolean isConnected(LineSection other) {
        return getDownStationId().equals(other.getUpStationId());
    }

    public Long getDownStationId() {
        return section.getDownStationId();
    }

    public Long getUpStationId() { return section.getUpStationId(); }

    public int getDistance() { return section.getDistance(); }

    public Long lineId() { return line.getId(); }
}
