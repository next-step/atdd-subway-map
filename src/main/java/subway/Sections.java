package subway;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Sections(Section section) {
        this.sections = Arrays.asList(section);
    }

    public void add(Section newSection) throws WrongSectionCreateException {
        validateNewSectionDownStation(newSection.getDownStation());
        validateNewSectionUpStation(newSection.getUpStation());

        this.sections.add(newSection);
    }

    private void validateNewSectionUpStation(Station newSectionUpStation) {
        Section downEndSection = getDownEndSection();
        if (Objects.nonNull(downEndSection) && !downEndSection.getDownStation().equals(newSectionUpStation)) {
            throw new WrongSectionCreateException("section's up station is not line's down end station");
        }
    }

    private void validateNewSectionDownStation(Station newSectionDownStation) {
        if (getStations().contains(newSectionDownStation)) {
            throw new WrongSectionCreateException("section's down station can't be in other section's station");
        }
    }

    public List<Station> getStations() {
        return getSections().stream().flatMap(section -> section.getStations().stream())
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section getDownEndSection() {
        if (CollectionUtils.isEmpty(sections)) {
            return null;
        }

        Section section = sections.get(0);
        while (true) {
            Section downSection = findNextDownSection(section);
            if (Objects.isNull(downSection)) {
                break;
            }

            section = downSection;
        }

        return section;
    }

    private Section findNextDownSection(final Section upSection) {
        return sections.stream().filter(section -> upSection.getDownStation().equals(section.getUpStation()))
                .findFirst()
                .orElse(null);
    }

    public void removeSection(Long stationId) throws WrongSectionDeleteException {
        if (sections.size() == 1) {
            throw new WrongSectionDeleteException("The line has only one section");
        }

        Section downEndSection = getDownEndSection();
        if (!downEndSection.getDownStation().getId().equals(stationId)) {
            throw new WrongSectionDeleteException("it is not the last section of line");
        }
        sections.remove(downEndSection);
    }
}
