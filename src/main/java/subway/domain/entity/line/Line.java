package subway.domain.entity.line;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import subway.domain.command.LineCommand;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineSection> sections;

    protected Line() {
    }

    @Builder
    public Line(String name, String color, List<LineSection> sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static Line init(LineCommand.CreateLine command) {
        Line line = new Line(command.getName(), command.getColor(), new ArrayList<>());
        LineSection section = new LineSection(line, command.getUpStationId(), command.getDownStationId(), command.getDistance());
        line.addSection(section);
        return line;
    }

    public void addSection(LineSection section) {
        this.sections.add(section);
    }


    public void update(LineCommand.UpdateLine command) {
        this.name = command.getName();
        this.color = command.getColor();
    }
}
