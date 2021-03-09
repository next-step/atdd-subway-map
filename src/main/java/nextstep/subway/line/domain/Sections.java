package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(){}

    public void addSection(Section section) {
        if (getStations().size() == 0) {
            sections.add(section);
            return;
        }

        boolean isNotValidUpStation = getStations().get(getStations().size() - 1).getId() != section.getUpStation().getId();
        if (isNotValidUpStation) {
            throw new RuntimeException("상행역은 하행 종점역이어야 합니다.");
        }

        boolean isDownStationExisted = getStations().stream().anyMatch(it -> it.getId() == section.getDownStation().getId());
        if (isDownStationExisted) {
            throw new RuntimeException("하행역이 이미 등록되어 있습니다.");
        }

        sections.add(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation(sections);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation(List<Section> sections) {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void removeSection(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException("해당 노선에 등록된 역이 없습니다");
        }

        Station downStation = sections.get(sections.size() -1).getDownStation();
        boolean isNotValidUpStation = downStation.getId() != station.getId();
        if (isNotValidUpStation) {
            throw new RuntimeException("하행 종점역만 삭제가 가능합니다.");
        }

        sections.stream()
                .filter(it -> it.getDownStation().getId() == station.getId())
                .findFirst()
                .ifPresent(it -> sections.remove(it));
    }
}
