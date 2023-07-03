package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public LineResponse getLine(Long id) {
        return new LineResponse(lineRepository.findById(id)
                                              .orElseThrow(LineNotFoundException::new));
    }

    @Transactional
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
}
