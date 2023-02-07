package subway.line.domain;

import subway.line.exception.DownStationAlreadyRegisteredException;
import subway.line.exception.NotSameAsRegisteredDownStationException;
import subway.line.exception.NotLastSectionException;
import subway.line.exception.SectionNotFoundException;
import subway.line.exception.SingleSectionException;
import subway.station.domain.Station;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = ALL, orphanRemoval = true)
    private final List<Section> sectionList = new ArrayList<>();

    /**
     * 지하철 노선에 포함된 구간들의 역 목록을 조회합니다.
     *
     * @return 지하철 노선에 포함된 역 목록 반환
     */
    public Set<Station> getStations() {
        Set<Station> stations = new HashSet<>();

        sectionList.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });

        return stations;
    }

    /**
     * 지하철 노선의 최초 구간을 등록합니다.
     *
     * @param section 등록할 지하철 구간 정보
     */
    public void createFirstSection(final Section section) {
        sectionList.add(section);
    }

    /**
     * 지하철 노선의 구간을 등록합니다.
     * 
     * @param section 등록할 지하철 구간 정보
     */
    public void addSection(final Section section) {
        if (isNotExistDownStation(section.getUpStation())) {
            throw new NotSameAsRegisteredDownStationException();
        }

        if (isAlreadyRegisteredStation(section.getDownStation())) {
            throw new DownStationAlreadyRegisteredException();
        }

        sectionList.add(section);
        section.getLine().changeDownStation(section.getDownStation());
    }

    /**
     * 지하철 노선의 마지막 구간을 삭제합니다.
     *
     * @param station 삭제할 지하철 구간의 역 정보
     */
    public void removeLastSection(final Station station) {
        Section lastSection = getLastSection();

        if (!lastSection.getDownStation().equals(station)) {
            throw new NotLastSectionException();
        }

        sectionList.remove(lastSection);
    }


    private Section getLastSection() {
        if (sectionList.isEmpty()) {
            throw new SectionNotFoundException();
        }

        if (sectionList.size() < 2) {
            throw new SingleSectionException();
        }

        return sectionList.get(sectionList.size() - 1);
    }

    private boolean isNotExistDownStation(Station station) {
        return sectionList.stream()
                .map(Section::getDownStation)
                .noneMatch(downStation -> downStation.equals(station));
    }

    private boolean isAlreadyRegisteredStation(final Station station) {
        return sectionList.stream()
                .anyMatch(section -> section.getUpStation().equals(station)
                        || section.getDownStation().equals(station));
    }
}
