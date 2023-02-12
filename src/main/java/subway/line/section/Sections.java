package subway.line.section;

import lombok.*;
import subway.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.*;

@Embeddable
@NoArgsConstructor
public class Sections {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    public Sections add(final Section section) {
        this.values.add(section);
        return this;
    }

    public Sections remove(final Station downStation) {
        this.values.removeIf(section -> section.isDownStation(downStation));
        return this;
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
