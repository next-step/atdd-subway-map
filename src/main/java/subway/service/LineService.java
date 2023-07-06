package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository,
        StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId())
            .orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
            .orElseThrow(IllegalArgumentException::new);

        Line line = new Line(lineRequest, upStation, downStation);
        upStation.setLine(line);
        downStation.setLine(line);

        Line savedLine = lineRepository.save(line);
        return createLineResponse(savedLine);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(this::createLineResponse)
            .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line);
    }
}
