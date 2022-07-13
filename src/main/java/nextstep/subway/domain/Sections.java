package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> values = new ArrayList<>();

    public static Sections create() {
        return new Sections();
    }

    public void add(Section section) {
        this.values.add(section);
    }

    public List<Station> stations() {
        return values.stream()
                .map(Section::stations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }
}
