package subway.line;

import org.springframework.stereotype.Service;
import subway.Station;
import subway.StationRepository;
import subway.StationResponse;

import javax.persistence.EntityNotFoundException;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse create(LineCreateRequest request) {

        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(
                () -> new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.")
        );
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(
                () -> new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.")
        );

        Line line = LineCreateRequest.toEntity(request);
        Line savedLine = lineRepository.save(line);

        StationResponse upStationResponse = StationResponse.of(upStation);
        StationResponse downStationResponse = StationResponse.of(downStation);

        return LineResponse.of(savedLine, upStationResponse, downStationResponse);
    }
}
