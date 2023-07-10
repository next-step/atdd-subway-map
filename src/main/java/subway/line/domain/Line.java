package subway.line.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import subway.section.domain.Section;
import subway.section.exception.AlreadyRegisteredStationException;
import subway.section.exception.InvalidSectionRegistrationException;
import subway.station.domain.Station;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;

        sections.add(section);
        section.assignLine(this);
    }

    public void addSection(Section newSection) {
        Section lastSection = getLastSection();
        validateLastStationEqualToNewUpStation(lastSection, newSection);
        validateDuplicationOfStationInLine(newSection);

        sections.add(newSection);
        newSection.assignLine(this);
    }

    private void validateLastStationEqualToNewUpStation(Section lastSection, Section newSection) {
        if (!lastSection.downStationEqualsToUpStationOf(newSection)) {
            throw new InvalidSectionRegistrationException();
        }
    }

    private void validateDuplicationOfStationInLine(Section newSection) {
        if (hasStation(newSection.getDownStation())) {
            throw new AlreadyRegisteredStationException();
        }
    }

    public void update(String name, String color) {
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

    public Section getLastSection() {
        sections.sort(Comparator.comparing(Section::getId));
        return sections.get(sections.size() - 1);
    }

    public boolean hasStation(Station downStation) {
        return sections.stream()
                .anyMatch(section -> section.hasStation(downStation));
    }

    public boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }
}
