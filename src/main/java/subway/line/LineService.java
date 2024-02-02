package subway.line;

import org.springframework.stereotype.Service;
import subway.Station;
import subway.StationRepository;
import subway.StationResponse;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse create(LineCreateRequest request) {

        Station upStation = getStation(request.getUpStationId());
        Station downStation = getStation(request.getDownStationId());

        Line line = LineCreateRequest.toEntity(request);
        Line savedLine = lineRepository.save(line);

        StationResponse upStationResponse = StationResponse.of(upStation);
        StationResponse downStationResponse = StationResponse.of(downStation);

        return LineResponse.of(savedLine, upStationResponse, downStationResponse);
    }

    public List<LineResponse> getLines() {

        List<Line> lines = lineRepository.findAll();
        List<LineResponse> responses = new ArrayList<>();

        for (Line line : lines) {
            Station upStation = getStation(line.getUpStationId());
            Station downStation = getStation(line.getDownStationId());

            StationResponse upStationResponse = StationResponse.of(upStation);
            StationResponse downStationResponse = StationResponse.of(downStation);

            LineResponse response = LineResponse.of(line, upStationResponse, downStationResponse);
            responses.add(response);
        }

        return responses;
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(
                () -> new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.")
        );
    }


}
