package subway.line.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineRepository;
import subway.line.domain.Line;
import subway.line.view.LineCreateRequest;
import subway.line.view.LineModifyRequest;
import subway.line.view.LineResponse;
import subway.station.Station;
import subway.station.StationNotFoundException;
import subway.station.StationService;

@Service
@RequiredArgsConstructor
public class LineService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    @Transactional
    public LineResponse createStation(LineCreateRequest request) {
        Optional<Station> upStation = stationService.findById(request.getUpStationId());

        if (upStation.isEmpty()) {
            throw new StationNotFoundException();
        }

        Optional<Station> downStation = stationService.findById(request.getDownStationId());

        if (downStation.isEmpty()) {
            throw new StationNotFoundException();
        }

        Line line = new Line();
        line.setName(request.getName());
        line.setColor(request.getColor());
        line.setUpStation(upStation.get());
        line.setDownStation(downStation.get());
        line.setDistance(request.getDistance());

        Line createdLine = lineRepository.save(line);

        return new LineResponse(createdLine);
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        return new LineResponse(lineRepository.findById(id)
                                              .orElseThrow(LineNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getList() {
        return lineRepository.findAll()
                             .stream()
                             .map(LineResponse::new)
                             .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void modifyLine(Long id, LineModifyRequest request) {
        Line line = lineRepository.findById(id).get();

        line.setName(request.getName());
        line.setColor(request.getColor());

        lineRepository.save(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
