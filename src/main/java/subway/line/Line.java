package subway.line;

import subway.section.Section;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    public Line() {}

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getColor() { return color; }

    public List<Section> getSections() { return this.sections; }

    public void setLineInSection(Section section) {
        section.setLine(this);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public boolean deleteSectionByStation(Station station) {
        List<Section> sections = getSections();
        Section lastSection = sections.get(sections.size() -1);

        if (lastSection.getDownStation().getId().equals(station.getId())) {
            sections.remove(lastSection);
            return true;
        }
        return false;
    }

    public void beforeAddSection(Section newSection) {
        List<Section> sections = this.getSections();
        Section lastSection = sections.get(sections.size()-1);
        Station newUpStation = newSection.getUpStation();
        Station newDownStation = newSection.getDownStation();

        if (!newUpStation.getId().equals(lastSection.getDownStation().getId())) {
            throw new IllegalArgumentException("새로운 구간의 상행역이 노선의 하행 종점역이 아닙니다.");
        }

        for (Section savedSection : sections) {
            Station savedUpStation = savedSection.getUpStation();
            Station savedDownStation = savedSection.getDownStation();
            if (newDownStation.getId().equals(savedUpStation.getId()) ||
                    newDownStation.getId().equals(savedDownStation.getId())) {
                throw new IllegalArgumentException("요청한 하행역은 이미 등록되어 있는 역입니다.");
            }
        }
    }
}