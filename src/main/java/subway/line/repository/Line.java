package subway.line.repository;

import lombok.*;
import subway.section.policy.AddSectionPolicy;
import subway.section.policy.DeleteSectionPolicy;
import subway.section.repository.Section;
import subway.station.repository.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public
class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "line_id")
    private List<Section> sections = new ArrayList<>();
    @Column(name = "total_distance", nullable = false)
    private Long totalDistance;

    @Builder
    Line(String name, String color, Section initSection) {
        this.name = name;
        this.color = color;
        this.sections.add(initSection);
        this.totalDistance = initSection.getDistance();
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }

    public List<Station> getAllStation() {
        List<Station> totalStation = this.sections.stream().map(Section::getUpStation).collect(Collectors.toList());
        totalStation.add(this.sections.get(this.sections.size() -1).getDownStation());
        return Collections.unmodifiableList(totalStation);
    }

    public void addSection(Section section) {
        AddSectionPolicy.validate(this, section);
        this.totalDistance += section.getDistance();
        this.sections.add(section);
    }

    public void deleteSectionByLastStation(Station station) {
        DeleteSectionPolicy.validate(this, station);
        this.totalDistance -= getLastSection().getDistance();
        this.sections.remove(getLastSection());
    }

    public Section getLastSection() {
        return this.sections.get(this.sections.size() -1);
    }


    public Station getDownEndStation() {
        return this.sections.get(this.sections.size() -1).getDownStation();
    }
}
