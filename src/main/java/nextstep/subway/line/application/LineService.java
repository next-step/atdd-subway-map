package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineRepository.save(new Line(request.getName(),
                                                        request.getColor(),
                                                        upStation, downStation, request.getDistance()));
        return createLineResponse(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                    .map(this::createLineResponse)
                    .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchLineException::new);
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        List<StationResponse> stations = getStations(line).stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations, line.getCreatedDate(), line.getModifiedDate());
    }

    public void updateLine(Long id, LineRequest request) {
        Optional<Line> line = lineRepository.findById(id);
        if (!line.isPresent()) {
            throw new NoSuchElementException("수정할 노선이 없습니다.");
        }
        lineRepository.save(request.toLine());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public void addSectionToLine(Long id, SectionRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchLineException::new);
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        checkSectionAddValidity(line, upStation, downStation);
        line.getSections().add(new Section(line, upStation, downStation, request.getDistance()));
    }

    private void checkSectionAddValidity(Line line, Station newUpStation, Station newDownStation) {
        List<Station> stations = getStations(line);
        if (stations.isEmpty()) {
            return;
        }
        if (isNotValidUpStation(newUpStation, stations)) {
            throw new SectionValidationException("새로운 구간의 상행역은 기존 하행 종점역이어야 합니다.");
        }
        if (isNotValidDownStation(newDownStation, stations)) {
            throw new SectionValidationException("하행역이 이미 등록되어 있습니다.");
        }
    }

    private boolean isNotValidUpStation(Station newUpStation, List<Station> stations) {
        return stations.get(stations.size() - 1).getId() != newUpStation.getId();
    }

    private boolean isNotValidDownStation(Station newDownStation, List<Station> stations) {
        return stations.stream().anyMatch(it -> it.getId() == newDownStation.getId());
    }

    public void deleteStationFromLine(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(NoSuchLineException::new);
        checkStationDeleteValidity(line, stationId);
        line.getSections().stream()
                .filter(it -> it.getDownStation().getId() == stationId)
                .findFirst()
                .ifPresent(it -> line.getSections().remove(it));
    }

    private void checkStationDeleteValidity(Line line, Long stationId) {
        if (line.getSections().size() <= 1) {
            throw new SectionValidationException("구간이 1개인 경우 역을 삭제할 수 없습니다.");
        }

        List<Station> stationsOfLine = getStations(line);
        int lastIndex = stationsOfLine.size() - 1;
        if (stationsOfLine.get(lastIndex).getId() != stationId) {
            throw new SectionValidationException("하행 종점역만 삭제가 가능합니다.");
        }
    }

    private List<Station> getStations(Line line) {
        List<Station> stations = new ArrayList<>();

        if (line.getSections().isEmpty()) {
            return stations;
        }

        Station downStation = findUpStation(line);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.getSections().stream()
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

    private Station findUpStation(Line line) {
        Station downStation = line.getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.getSections().stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }

}
