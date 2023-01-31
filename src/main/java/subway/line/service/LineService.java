package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.repository.Line;
import subway.line.repository.LineRepository;
import subway.station.repository.Station;
import subway.station.service.StationService;

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
        Line line = lineRepository.save(toLineEntity(lineRequest));

        return LineResponse.of(line);
    }

    private Line toLineEntity(LineRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());

        return new Line(lineRequest.getName(),
                lineRequest.getColor(),
                upStation,
                downStation,
                lineRequest.getDistance());
    }
}
