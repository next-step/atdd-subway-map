package subway.line;

import subway.linesection.LineSection;
import subway.linesection.LineSections;
import subway.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @ManyToOne(fetch = FetchType.LAZY)
    private Station upStation;
    @ManyToOne(fetch = FetchType.LAZY)
    private Station downStation;
    @Embedded
    private LineSections sections;

    protected Line() {
    }

    public static Line of(Long id, String name, String color, Station upStation, Station downStation, Integer distance) {
        Line line = Line.of(name, color, upStation, downStation, distance);
        line.id = id;
        return line;
    }

    public static Line of(String name, String color, Station upStation, Station downStation, Integer distance) {
        Line line = new Line();
        line.name = name;
        line.color = color;
        line.upStation = upStation;
        line.downStation = downStation;
        line.sections = LineSections.of(line, upStation, downStation, distance);
        return line;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LineSections getSections() {
        return sections;
    }

    public void addSection(LineSection section) {
        this.sections.add(section);
        this.downStation = section.getDownStation();
    }

    public void removeSection(Station toDeleteStation) {
        this.sections.remove(toDeleteStation);
        this.downStation = this.sections.getLastLineSection().getDownStation();
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
