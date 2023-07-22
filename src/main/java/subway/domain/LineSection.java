package subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import subway.exception.LineNotConnectableException;

@Entity
public class LineSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @ManyToOne(cascade = CascadeType.ALL)
    private Section section;

    public LineSection() {}

    public LineSection(Line line, Section section) {
        validateConnectable(line, section);
        this.line = line;
        this.section = section;
    }

    private void validateConnectable(Line line, Section section) {
        if (!line.isDownEndStation(section.getUpStation())) {
            throw new LineNotConnectableException("새로운 노선은 라인의 하행종착역을 상행역으로 설정해야 합니다: " + line.getEndStations().downEndStation().getId());
        }
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Section getSection() {
        return section;
    }
}
