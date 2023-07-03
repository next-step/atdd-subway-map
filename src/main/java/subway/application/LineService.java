package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.exception.LineDuplicationNameException;
import subway.exception.StationNotFoundException;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse save(LineCreateRequest lineCreateRequest) {
        validateDuplicationLineName(lineCreateRequest.getName());
        Line line = lineRepository.save(lineCreateRequest.toLine());
        Station upStation = getStation(lineCreateRequest.getUpStationId());
        Station downStation = getStation(lineCreateRequest.getDownStationId());
        line.addSection(upStation, downStation, lineCreateRequest.getDistance());
        return LineResponse.of(line);
    }

    private void validateDuplicationLineName(String name) {
        if (lineRepository.existsByName(name)) {
            throw new LineDuplicationNameException();
        }
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(StationNotFoundException::new);
    }
}
