package subway.section;

import subway.line.Line;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    public Section() {}

    public Section(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Line getLine() {
        return line;
    }

    public void mappingLine(Line line) {
        this.line = line;
        line.getSections().add(this);
    }
}
