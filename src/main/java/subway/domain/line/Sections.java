package subway.domain.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.domain.*;
import subway.exception.*;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableList;

@Getter
@NoArgsConstructor
@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void createSection(CreateSectionVo createSectionVo) {
        Section section = createSectionVo.getSection();
        sections.add(section);
        section.createSectionStations(new CreateSectionStationVo(createSectionVo));
    }

    public void deleteAllSection() {
        for (Section section : sections) {
            section.deleteAllSectionStation();
        }
        sections.clear();
    }

    public void addSection(AddSectionVo addSectionVo) {
        Section section = addSectionVo.getSection();
        sections.add(section);
        section.addSectionStations(new AddSectionStationVo(addSectionVo));
    }

    public void deleteSection(DeleteSectionVo deleteSectionVo) {
        Section section = deleteSectionVo.getSection();
        section.deleteSectionStation(new DeleteSectionStationVo(deleteSectionVo));
        sections.remove(section);
    }

    public List<Station> getStationsByAscendingOrder() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<UpAndDownStationsVo> upAndDownStations = sections.stream()
                .map(Section::getUpAndDownStations)
                .collect(toUnmodifiableList());

        Station upStation = upAndDownStations.get(0).getUpStation();
        Station downStation = upAndDownStations.get(0).getDownStation();

        List<Station> stations = new ArrayList<>();
        stations.addAll(getUpperStations(upAndDownStations, upStation));
        stations.addAll(getDownerStations(upAndDownStations, downStation));

        return Collections.unmodifiableList(stations);
    }

    private List<Station> getUpperStations(List<UpAndDownStationsVo> upAndDownStations, Station upStation) {
        List<Station> upperStations = new ArrayList<>();
        upperStations.add(upStation);

        Optional<UpAndDownStationsVo> upAndDownStation =
                upAndDownStations.stream()
                        .filter(dto -> dto.getDownStation().equals(upStation))
                        .findFirst();
        while (upAndDownStation.isPresent()) {
            Station upperStation = upAndDownStation.get().getUpStation();
            upperStations.add(upperStation);
            upAndDownStation =
                    upAndDownStations.stream()
                            .filter(dto -> dto.getDownStation().equals(upperStation))
                            .findFirst();
        }

        Collections.reverse(upperStations);
        return upperStations;
    }

    private List<Station> getDownerStations(List<UpAndDownStationsVo> upAndDownStations, Station downStation) {
        List<Station> downerStations = new ArrayList<>();
        downerStations.add(downStation);

        Optional<UpAndDownStationsVo> upAndDownStation =
                upAndDownStations.stream()
                        .filter(dto -> dto.getUpStation().equals(downStation))
                        .findFirst();
        while (upAndDownStation.isPresent()) {
            Station downerStation = upAndDownStation.get().getDownStation();
            downerStations.add(downerStation);
            upAndDownStation =
                    upAndDownStations.stream()
                            .filter(dto -> dto.getUpStation().equals(downerStation))
                            .findFirst();
        }

        return downerStations;
    }

    public void checkSectionExtendValid(Station upStation, Station downStation) {
        List<Station> stations = getStationsByAscendingOrder();
        if (!upStation.equals(stations.get(stations.size() - 1))) {
            throw new ExtendSectionException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.");
        }
        if (stations.stream().anyMatch(station -> station.equals(downStation))) {
            throw new ExtendSectionException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.");
        }
    }

    public void checkSectionReduceValid(Station station) {
        List<Station> stations = getStationsByAscendingOrder();
        if (stations.isEmpty()) {
            throw new ReduceSectionException("해당 노선이 비어있습니다.");
        }
        if (!stations.get(stations.size() - 1).equals(station)) {
            throw new ReduceSectionException("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.");
        }
        if (stations.size() == 2) {
            throw new ReduceSectionException("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.");
        }
    }
}
