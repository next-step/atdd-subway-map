package subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    private static final String ADD_ERROR_MESSAGE = "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.";
    private static final String ADD_DUPLICATE_ERROR_MESSAGE = "새로운 구간의 하행역이 해당 노선에 등록되어있으면 안됩니다.";

    @OneToMany(mappedBy = "lineId", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if (!sections.isEmpty()) {
            if (!sections.get(sections.size() - 1).isDownStation(section.getUpStation())) {
                throw new IllegalArgumentException(ADD_ERROR_MESSAGE);
            }
            if (getStations().contains(section.getDownStation())) {
                throw new IllegalArgumentException();
            }
        }
        sections.add(section);
    }

    public Set<Station> getStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(Collectors.toSet());
    }
}
