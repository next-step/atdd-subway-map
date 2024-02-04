package subway.line;

import subway.line.section.CannotAddSectionException;
import subway.line.section.CannotDeleteSectionException;
import subway.line.section.Section;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public List<Section> getSections() {
        return sections;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void addSection(Section section) throws CannotAddSectionException {
        if (!sections.isEmpty()) {
            Station downStation = sections.get(sections.size() - 1).getDownStation();
            if (!downStation.equals(section.getUpStation())) {
                throw new CannotAddSectionException("노선 등록 시 상행역은 현재 하행 종점역이어야 합니다.");
            }
            List<Station> allStation = sections.stream().flatMap(section1 -> Stream.of(section1.getUpStation(), section1.getDownStation())).distinct().collect(Collectors.toList());
            if (allStation.contains(section.getDownStation())) {
                throw new CannotAddSectionException("이미 구간에 등록된 역입니다.");
            }
        }
        sections.add(section);
    }

    public void deleteSection(Long stationId) throws CannotDeleteSectionException {
        if (sections.size() == 1)
            throw new CannotDeleteSectionException("구간이 한개인 경우 삭제할 수 없습니다.");
        Station downStation = sections.get(sections.size() - 1).getDownStation();
        if (!downStation.getId().equals(stationId)) {
            throw new CannotDeleteSectionException("삭제 역이 하행 종점역이 아닙니다.");
        }
        sections.remove(sections.size() - 1);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                '}';
    }
}
