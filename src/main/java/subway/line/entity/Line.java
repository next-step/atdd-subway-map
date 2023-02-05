package subway.line.entity;

import lombok.Getter;
import subway.line.dto.request.LineRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "lines")
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "line_sections", joinColumns = @JoinColumn(name = "line_id"), inverseJoinColumns = @JoinColumn(name = "section_id"))
    private final List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(LineRequest lineRequest, Station upStation, Station downStation) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.upStation = upStation;
        this.downStation = downStation;
        this.sections.add(new Section(lineRequest.getDistance(), upStation, downStation));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void appendSection(Section section) {
        this.sections.add(section);
        this.downStation = section.getDownStation();
    }

    public void removeSection() {
        this.sections.remove(this.sections.size() - 1);

        Section lastSection = this.sections.get(this.sections.size() - 1);
        this.downStation = lastSection.getDownStation();
    }
}
