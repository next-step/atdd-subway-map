package nextstep.subway.domain;

import nextstep.subway.exception.DeleteStationException;
import nextstep.subway.exception.StationNotRegisteredException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        if (onlyOneStationExist()) {
            sections.get(0).addDownStation(section.getDownStation());
            return;
        }
        sections.add(section);
    }

    public Station firstStation() {
        return sections.get(0).getUpStation();
    }

    public List<Station> allStations() {
        if (onlyOneStationExist()) {
            return List.of(firstStation());
        }

        return Stream.concat(
                        Stream.of(firstStation()),
                        sections.stream().map(Section::getDownStation)
                )
                .collect(Collectors.toList());
    }

    public boolean hasStation(Station station) {
        return sections.stream().anyMatch(section -> section.hasStation(station));
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Station lastStation() {
        if (sections.isEmpty()) {
            throw new StationNotRegisteredException("노선에 등록된 역이 없습니다.");
        }
        return sections.get(sections.size() - 1).getDownStation();
    }

    public void delete(Station station) {
        validateDelete(station);
        if (sections.size() == 1) {
            sections.get(0).deleteDownStation();
            return;
        }
        sections.remove(sections.get(sections.size() - 1));
    }

    private void validateDelete(Station station) {
        if (onlyOneStationExist()) {
            throw new DeleteStationException("상행역과 하행역만 존재하기 때문에 삭제할 수 없습니다.");
        }

        if (notLastStation(station)) {
            throw new DeleteStationException("하행역만 삭제할 수 있습니다.");
        }
    }

    private boolean onlyOneStationExist() {
        return sections.size() == 1 && sections.get(0).getDownStation() == null;
    }

    private boolean notLastStation(Station station) {
        return !sections.get(sections.size() - 1).getDownStation().equals(station);
    }
}
