package subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.Station;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LineSections {
    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private final List<Section> value = new ArrayList<>();

    private static final int MINIMUM_SECTION_SIZE = 2;

    public static LineSections init(final Section section) {
        final var sections = new LineSections();
        sections.value.add(section);
        return sections;
    }

    public void append(final Section section) {
        requireSectionAppendable(section);
        value.add(section);
    }

    private void requireSectionAppendable(final Section section) {
        if (!getLastStation().equalsId(section.getUpStation())) {
            throw new IllegalArgumentException(String.format(
                    "구간 등록 실패) 노선의 하행역이 구간의 상행역과 일치하지 않습니다 : 노선의 하행역 id=%d, 구간의 상행역 id=%d",
                    getLastStation().getId(), section.getUpStation().getId()
            ));
        }
        if (getStations().contains(section.getDownStation())) {
            throw new IllegalArgumentException(
                    "구간 등록 실패) 구간의 하행역이 이미 노선에 포함되어 있습니다 : 구간의 하행역 id=" + section.getDownStation().getId()
            );
        }
    }

    public void remove(final Station station) {
        requireStationRemovable(station);
        value.remove(getLastSection());
    }

    private void requireStationRemovable(final Station station) {
        if (!getLastStation().equalsId(station)) {
            throw new IllegalArgumentException("구간 삭제 실패) 노선의 하행역이 아닙니다 : 역 id=" + station.getId());
        }
        if (getStations().size() == MINIMUM_SECTION_SIZE) {
            throw new IllegalArgumentException("구간 삭제 실패) 상행 종점역과 하행 종점역만 있습니다");
        }
    }

    public Station getFirstStation() {
        return getFirstSection().getUpStation();
    }

    private Section getFirstSection() {
        checkSizeNotEmpty();
        return value.get(0);
    }

    public Station getLastStation() {
        return getLastSection().getDownStation();
    }

    private Section getLastSection() {
        checkSizeNotEmpty();
        return value.get(value.size() - 1);
    }

    private void checkSizeNotEmpty() {
        if (this.value.isEmpty()) {
            throw new IllegalStateException("노선에 등록된 구간이 없습니다");
        }
    }

    public List<Station> getStations() {
        final var upStation = getFirstStation();
        final var downStations = value.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toUnmodifiableList());

        final var stations = new ArrayList<Station>();
        stations.add(upStation);
        stations.addAll(downStations);
        return stations;
    }
}
