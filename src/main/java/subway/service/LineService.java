package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.line.LineRequest;
import subway.dto.line.LineResponse;
import subway.exception.SubwayException;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

import static subway.exception.SubwayExceptionStatus.STATION_NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());

        Line newLine = new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
        lineRepository.save(newLine);
        return LineResponse.from(newLine);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse findLineById(Long id) {
        return LineResponse.from(findLine(id));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findLine(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        Line line = findLine(id);
        lineRepository.delete(line);
    }

    private Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new SubwayException(id + " 노선을 찾을 수 없습니다.", STATION_NOT_FOUND));
    }
}
