package subway.domain;

import lombok.Getter;
import subway.domain.DomainException.LineException;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private final Set<Section> sections = new HashSet<>();

    public Line() {
    }

    public Line(String name, String color, int distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;

        Section section = new Section(this, upStation.getId(), downStation.getId(), distance);
        sections.add(section);
    }


    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void registSection(Section section) {
        validateNewSectionStationNotInExistingInStations(section);
        Section lastSection = getLatestSections().stream()
                .findFirst()
                .orElseThrow(LineException::InvalidSectionException);
        if (!lastSection.getDownStationId().equals(section.getUpStationId())) {
            throw LineException.InvalidSectionException();
        }

        sections.add(section);
    }

    private Optional<Section> getLatestSections() {
        return sections.stream().max(Comparator.comparing(Section::getId));
    }

    private void validateNewSectionStationNotInExistingInStations(Section newSection) {
        if (sections.stream()
                .anyMatch(
                        section -> section.isUpStationOrDownStation(newSection.getUpStationId(), newSection.getDownStationId()))) {
            throw LineException.AlreadyRegisteredStationException();
        }
    }

    public void deleteSection(Long stationId) {
        if (sections.size() == 1) {
            throw LineException.NotRemoveException();
        }
        getLatestSections()
                .ifPresent(section -> {
                    if (!section.getDownStationId().equals(stationId)) {
                        throw LineException.NotRemoveException();
                    }
                    sections.remove(section);
                });
    }

    public Set<Long> getStationIds() {
        Set<Long> stationIds = new HashSet<>();
        for (Section section : sections) {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        }
        return stationIds;
    }
}
