package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import nextstep.subway.exception.CannotDeleteSectionSizeUnderTwo;
import nextstep.subway.exception.SectionsEmptyException;

@Embeddable
public class Sections {

    private static final int COUNT_CAN_BE_DELETED = 2;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "section_id")
    private final List<Section> sections = new LinkedList<>();

    protected Sections() {
    }

    public Sections(Section section) {
        this.sections.add(section);
    }

    public void register(Station upStation, Station downStation, int distance) {
        sections.add(new Section(upStation, downStation, distance));
    }

    public boolean isLastStaion(Station station) {
        Section lastSection = getLastSection();

        return lastSection.isDownStation(station);
    }

    private Section getLastSection() {
        if (sections.isEmpty()) {
            throw new SectionsEmptyException();
        }
        return sections.get(sections.size() - 1);
    }

    public boolean contianStation(Station station) {
        return sections.stream()
            .anyMatch(section -> section.containStation(station));
    }


    public void deleteSection(Station station) {
        validateSectionsCount();

        Section section = getLastSection();

        section.isDownStation(station);

        sections.remove(section);
    }

    private void validateSectionsCount() {
        if (sections.size() < COUNT_CAN_BE_DELETED) {
            throw new CannotDeleteSectionSizeUnderTwo();
        }
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        sections.forEach(station -> stations.addAll(station.getStations()));

        return stations.stream().distinct().collect(Collectors.toList());
    }

}
