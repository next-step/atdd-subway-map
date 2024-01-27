package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Station.Station;
import subway.Station.StationRepository;
import subway.Station.StationResponse;

import java.util.List;

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
        Line line = lineRepository.save(
                new Line(
                        lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()
                )
        );

        Station upStation = stationRepository.getById(lineRequest.getUpStationId());
        Station downStation = stationRepository.getById(lineRequest.getDownStationId());

        return createLineResponse(line, upStation, downStation);
    }

    private LineResponse createLineResponse(Line line, Station upStation, Station downStation) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(
                        new StationResponse(upStation.getId(), upStation.getName()),
                        new StationResponse(downStation.getId(), downStation.getName())
                ),
                line.getDistance()
        );
    }

}
