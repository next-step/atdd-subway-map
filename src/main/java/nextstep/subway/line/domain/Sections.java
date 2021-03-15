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
        validateAddSection(section);
        sections.add(section);
    }

    private void validateAddSection(Section section){
        List<Station> stations = getStations();
        if (getStations().size() == 0) {
            return;
        }
        if (isNotValidUpStation(stations, section)) {
            throw new RuntimeException("상행역은 하행 종점역이어야 합니다.");
        }

        if (isDownStationExisted(stations, section)) {
            throw new RuntimeException("하행역이 이미 등록되어 있습니다.");
        }
    }


    private boolean isDownStationExisted(List<Station> stations, Section section) {
        return stations.stream().anyMatch(it -> it.equals(section.getDownStation()));
    }

    private boolean isNotValidUpStation(List<Station> stations, Section section) {
        return !stations.get(stations.size() - 1).equals(section.getUpStation());
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

    public void removeSection(Long stationId) {
        if (sections.size() <= 1) {
            throw new RuntimeException("해당 노선에 등록된 역이 없습니다");
        }

        Station downStation = sections.get(sections.size() -1).getDownStation();
        boolean isNotValidUpStation = downStation.getId() != stationId;
        if (isNotValidUpStation) {
            throw new RuntimeException("하행 종점역만 삭제가 가능합니다.");
        }

        sections.stream()
                .filter(it -> it.getDownStation().getId() == stationId)
                .findFirst()
                .ifPresent(it -> sections.remove(it));
    }
}
