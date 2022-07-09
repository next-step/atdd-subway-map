package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private Set<Section> values = new LinkedHashSet<>();

    private Sections(Section section) {
        this.values.add(section);
    }

    public static Sections create(Section section) {
        return new Sections(section);
    }

    public List<Station> stations() {
        return values.stream()
                .map(Section::stations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}
