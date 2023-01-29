package subway.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;

@Service
public class LineService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    public LineService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse createLine(LineRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance()));

        return LineResponse.createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(LineResponse::createLineResponse)
            .collect(toList());
    }

    public LineResponse findLineById(Long lineId) {
        return lineRepository.findById(lineId)
            .map(LineResponse::createLineResponse)
            .orElseThrow(LineNotFoundException::new);
    }
}
