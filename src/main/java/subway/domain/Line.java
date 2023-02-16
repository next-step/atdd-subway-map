package subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Embedded
    private Sections sections = new Sections();

    private int lineDistance;

    public List<Station> getStations() {
        List<Section> sections = this.sections.getSectionList();

        var upStations = sections.stream().map(Section::getUpStation);
        var downStations = sections.stream().map(Section::getDownStation);

        return Stream.concat(upStations, downStations)
            .distinct()
            .collect(Collectors.toList());
    }

    public void modify(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @Builder(builderMethodName = "GenerateLine")
    public Line(String name, String color, int distance) {
        this.name = name;
        this.color = color;
        this.lineDistance = distance;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        this.lineDistance = sections.getTotalDistance();
    }

    public void deleteSection(Station station) {
        this.sections.delete(station);
        this.lineDistance = sections.getTotalDistance();
    }
}
