package nextstep.subway.line.application;

import nextstep.subway.common.SectionValidationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void updateLine(Long id, LineRequest request) {
        Optional<Line> line = lineRepository.findById(id);
        if (!line.isPresent()) {
            throw new NoSuchElementException("no line to update!");
        }
        lineRepository.save(request.toLine());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public void addSectionToLine(Long id, SectionRequest request) {
        Line line = findLineById(id);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        checkSectionValidity(line, upStation, downStation);
        line.getSections().add(new Section(line, upStation, downStation, request.getDistance()));
    }

    private void checkSectionValidity(Line line, Station newUpStation, Station newDownStation) {
        List<Station> stations = getStations(line);
        if (stations.isEmpty()) return;
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
        return stations.stream().anyMatch(x -> x.getId() == newDownStation.getId());
    }

    public List<Station> getStations(Line line) {
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
