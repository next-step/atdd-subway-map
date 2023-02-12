package subway.line.section;

import lombok.*;
import subway.*;

import javax.persistence.*;
import java.util.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sections {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    public Sections(final Section section) {
        final ArrayList<Section> sections = new ArrayList<>();
        sections.add(section);
        this.values = sections;
    }

    public Sections add(final Section section) {

        validationLastStation(section.getUpStation());

        if (this.anyMatchStation(section.getDownStation())) {
            throw new DuplicateSectionStationException();
        }

        this.values.add(section);
        return this;
    }

    private void validationLastStation(final Station section) {
        if (!this.isLastStation(section)) {
            throw new NotLastDownStationException();
        }
    }

    public Sections remove(final Station downStation) {
        validationLastStation(downStation);
        if(isOnlyOne()) {
            throw new OnlyOneSectionException();
        }

        this.values.removeIf(section -> section.isDownStation(downStation));
        return this;
    }

    private boolean isOnlyOne() {
        return this.values.size() == 1;
    }

    public List<Section> getValues() {

        return List.copyOf(this.values);
    }

    public boolean isLastStation(final Station station) {

        return getLastSection()
                .map(section -> section.isDownStation(station))
                .orElse(false);
    }

    private Optional<Section> getLastSection() {
        if(this.values.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.values.get(lastIndex()));
    }

    private int lastIndex() {
        return this.values.size() - 1;
    }

    public boolean anyMatchStation(final Station station) {
        return this.values.stream()
                .anyMatch(section -> section.anyMatchStation(station));
    }
}
