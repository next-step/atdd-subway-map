package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationService;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.findById(lineRequest.getUpStationId());
        Station downStation = stationService.findById(lineRequest.getDownStationId());
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance()));
        return LineResponse.of(line);
    }
}
