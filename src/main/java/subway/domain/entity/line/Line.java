package subway.domain.entity.line;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import subway.domain.command.LineCommand;

import javax.persistence.*;

@ToString
@Getter
@Entity(name = "lines")
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String color;

    @Embedded
    private LineSections sections;

    protected Line() {
    }

    @Builder
    public Line(String name, String color, LineSections sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static Line init(LineCommand.CreateLine command) {
        Line line = new Line(command.getName(), command.getColor(), new LineSections());
        line.addSection(command.getUpStationId(), command.getDownStationId(), command.getDistance());
        return line;
    }


    public void update(LineCommand.UpdateLine command) {
        this.name = command.getName();
        this.color = command.getColor();
    }

    public void addSection(Long upStationId, Long downStationId, Long distance) {
        LineSection section = new LineSection(this, upStationId, downStationId, distance);
        sections.addSection(section);
    }

    public void deleteSection(Long stationId) {
        sections.deleteSection(stationId);
    }
}
