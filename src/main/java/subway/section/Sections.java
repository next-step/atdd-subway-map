package subway.section;

import subway.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void createSection(Section section) {
        if (!findLastStation().isDownStation(section.getUpStation())) {
            throw new SectionCannotCreateException("UpStation is not a registered downStation.");
        }

        if (isAlreadyExistsStation(section.getDownStation())) {
            throw new SectionAlreadyExistsException(section.getDownStation().getId());
        }

        sections.add(section);
    }

    private Section getLastSection() {
        if (sections.isEmpty()) {
            throw new SectionCannotDeleteException("Section is empty.");
        }

        return sections.get(sections.size() - 1);
    }

    private Station getLastStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    private Section findLastStation() {
        return sections.get(sections.size() - 1);
    }

    public void deleteSection(Long downStationId) {
        validationDeleteSection(downStationId);
        sections.remove(getLastSection());
    }

    private void validationDeleteSection(Long stationId) {
        if (getLastStation().getId() != stationId) {
            throw new SectionCannotDeleteException(stationId);
        }
    }

    private boolean isAlreadyExistsStation(Station downStation) {
        return sections.stream()
                .anyMatch(section -> section.equals(downStation));
    }
}
