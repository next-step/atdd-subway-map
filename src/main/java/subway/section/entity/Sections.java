package subway.section.entity;

import subway.station.entity.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "LINE_ID")
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section getFirstSection() {
        return sections.stream()
                .min(Comparator.comparing(Section::getPosition)).get();
    }

    public Section getLastSection() {
        return sections.stream()
                .max(Comparator.comparing(Section::getPosition)).get();
    }

    public Station getFirstStation() {
        return sections.stream()
                .min(Comparator.comparing(Section::getPosition))
                .get().getUpStation();
    }

    public Station getLastStation() {
        return sections.stream()
                .max(Comparator.comparing(Section::getPosition))
                .get().getDownStation();
    }

    public Long getCurrentSectionsPosition() {
        return (long) sections.size() - 1;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

}
