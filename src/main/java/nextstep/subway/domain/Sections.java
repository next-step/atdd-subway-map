package nextstep.subway.domain;

import nextstep.subway.exception.BadRequestException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if(sections.isEmpty()) {
            sections.add(section);
            return;
        }

        validateAddSection(section);
        sections.add(section);
    }

    private void validateAddSection(Section section) {
        if(!isDownStation(section.getUpStation())) {
            throw new BadRequestException("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
        }

        if(isRegisteredStation(section.getDownStation())) {
            throw new BadRequestException("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없습니다.");
        }
    }

    public void removeSection(Station station) {
        validateDeleteSection(station);
        sections.remove(getLastSection());
    }

    private void validateDeleteSection(Station station) {
        if(!canDelete()) {
            throw new BadRequestException("지하철 노선의 구간이 1개인 경우 구간을 삭제할 수 없습니다.");
        }

        if(!isDownStation(station)) {
            throw new BadRequestException("지하철 노선에 등록된 마지막 역만 제거할 수 있습니다.");
        }
    }

    public List<Station> getAllStation() {
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(sections.get(sections.size() - 1).getDownStation());

        return stations;
    }

    public Station getDownStation() {
        return getAllStation().get(getAllStation().size() - 1);
    }

    public Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    public boolean isRegisteredStation(Station station) {
        return getAllStation().contains(station);
    }

    public boolean canDelete() {
        return sections.size() > 1;
    }

    public boolean isDownStation(Station station) {
        return getDownStation().equals(station);
    }

}
