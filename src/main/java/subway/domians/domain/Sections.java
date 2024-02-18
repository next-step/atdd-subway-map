package subway.domians.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public Optional<Station> getUpStation() {
        var first = this.sections.stream().findFirst();
        return first.map(Section::getUpStation);
    }

    public List<Station> getStations() {
        return this.sections.stream()
            .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
            .collect(Collectors.toList());
    }
}
