package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.exception.StationNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;

    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(StationNotFoundException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(StationNotFoundException::new);

        Line line = lineRepository.save(
                new Line(lineRequest.getName(),
                        lineRequest.getColor(),
                        upStation.getId(),
                        downStation.getId(),
                        lineRequest.getDistance()));

        return new LineResponse(line);
    }
}
