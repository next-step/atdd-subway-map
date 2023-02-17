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

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
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
        if (getStations().contains(newSection.getDownStation())) {
            throw new WrongSectionCreateException("section's down station can't be in other section's station");
        }

        Section downEndSection = getDownEndSection();
        if (Objects.nonNull(downEndSection) && !downEndSection.getDownStation().equals(newSection.getUpStation())) {
            throw new WrongSectionCreateException("section's up station is not line's down end station");
        }

        this.sections.add(newSection);
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
}
