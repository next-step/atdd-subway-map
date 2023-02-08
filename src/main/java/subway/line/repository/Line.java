package subway.line.repository;

import subway.section.repository.Section;
import subway.station.repository.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static subway.common.constants.ErrorConstant.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        setSection(section);
    }

    public Long removeSection(Long stationId) {
        Section lastSection = getLastSection();
        sections.remove(lastSection);

        return lastSection.getId();
    }

    public void addSection(Section section) {
        isAddValidation(section);
        setSection(section);
    }

    private void isAddValidation(Section section) {
        if (!getDownStation().equals(section.getUpStation())) {
            throw new IllegalArgumentException(NOT_ADD_LAST_STATION);
        }

        if (sections.stream().anyMatch(s -> s.getUpStation().equals(section.getDownStation()))) {
            throw new IllegalArgumentException(ALREADY_ENROLL_STATION);
        }
    }

    private void setSection(Section section) {
        section.setLine(this);
        sections.add(section);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    private Station getDownStation() {
        return getLastSection().getDownStation();
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    protected Line() {}

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
}
