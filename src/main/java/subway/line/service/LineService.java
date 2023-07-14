package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineDto;
import subway.line.entity.Line;
import subway.line.entity.LineRepository;
import subway.section.entity.Section;
import subway.section.entity.SectionRepository;
import subway.station.dto.StationDto;
import subway.station.entity.Station;
import subway.station.entity.StationRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public LineDto saveLine(LineDto lineDto) {
        Station upStation = getStation(lineDto.getUpStationId());
        Station downStation = getStation(lineDto.getDownStationId());

        Line savedLineEntity = lineRepository.save(lineDto.toEntity());

        sectionRepository.save(new Section(savedLineEntity, upStation, downStation, lineDto.getDistance()));

        List<Station> stations = getRelatedStations(savedLineEntity);

        return LineDto.from(savedLineEntity, stations.stream().map(StationDto::from).collect(Collectors.toList()));
    }

    public List<LineDto> getLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineDto.from(line, getRelatedStations(line).stream()
                                                  .map(StationDto::from)
                                                  .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public LineDto getLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("노선이 존재하지 않습니다. id:%s", id)));

        return LineDto.from(line, getRelatedStations(line).stream()
                                    .map(StationDto::from)
                                    .collect(Collectors.toList()));
    }

    @Transactional
    public void updateLine(Long id, LineDto dto) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("노선이 존재하지 않습니다. id:%s", id)));

        line.update(dto.getName(), dto.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Station getStation(Long lineDto) {
        Station upStation = stationRepository.findById(lineDto)
                .orElseThrow(() -> new IllegalArgumentException(String.format("역이 존재하지 않습니다. id:%s", lineDto)));
        return upStation;
    }

    public List<Station> getRelatedStations(Line line) {
        Set<Station> stationSet = new HashSet<>();
        sectionRepository.findAllByLine(line).stream()
                .forEach(s -> {
                    stationSet.add(s.getUpStation());
                    stationSet.add(s.getDownStation());
                });
        return stationSet.stream().collect(Collectors.toList());
    }

    private List<Station> getRelatedStationsInOrder(Line line) {

        List<Section> sectionList = sectionRepository.findAllByLine(line);
        Map<Station, Station> stationMap = new HashMap<>();
        Set<Station> downStations = new HashSet<>();

        sectionList.stream()
                .forEach(s -> {
                    stationMap.put(s.getUpStation(), s.getDownStation());
                    downStations.add(s.getDownStation());
                });

        // 시작 노드 찾기
        Station startStation = null;
        for (Station station : stationMap.keySet()) {
            if (!downStations.contains(station)) {
                startStation = station;
                break;
            }
        }

        if (startStation == null) {
            throw new IllegalArgumentException(String.format("호선 내 시작 노드가 없습니다. line id:%s", line.getId()));
        }

        // 시작노드부터 끝 노드까지 찾기
        List<Station> orderedStations = new ArrayList<>();
        Station currentStation = startStation;
        while (currentStation != null) {
            orderedStations.add(currentStation);
            currentStation = stationMap.get(currentStation);
        }

        return orderedStations;
    }

    public Station getDownStation(Line line) {
        List<Station> stationList = getRelatedStationsInOrder(line);
        return stationList.get(stationList.size() - 1);
    }
}
