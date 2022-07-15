package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section newSection) {
        if (!sections.isEmpty()) {
            validateAddSection(newSection);
        }
        sections.add(newSection);
    }

    private void validateAddSection(Section newSection) {
        // 이미 등록된 하행역인지

        // 상행역이 하행종점역이 아닌지
    }

    public List<Station> getAllStations() {
        return sections.stream()
                .map(Section::getStations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}