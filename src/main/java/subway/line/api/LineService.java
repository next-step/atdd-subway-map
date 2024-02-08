package subway.line.api;

import org.springframework.stereotype.Service;
import subway.line.presentation.request.LineCreateRequest;
import subway.line.api.response.LineResponse;
import subway.line.presentation.request.LineUpdateRequest;
import subway.line.domain.Line;
import subway.line.repository.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationResponse;

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

        Station upStation = getStationEntity(request.getUpStationId());
        Station downStation = getStationEntity(request.getDownStationId());

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
            Station upStation = getStationEntity(line.getUpStationId());
            Station downStation = getStationEntity(line.getDownStationId());

            StationResponse upStationResponse = StationResponse.of(upStation);
            StationResponse downStationResponse = StationResponse.of(downStation);

            LineResponse response = LineResponse.of(line, upStationResponse, downStationResponse);
            responses.add(response);
        }

        return responses;
    }

    public LineResponse getLine(Long lineId) {
        Line line = getLineEntity(lineId);

        Station upStation = getStationEntity(line.getUpStationId());
        Station downStation = getStationEntity(line.getDownStationId());

        StationResponse upStationResponse = StationResponse.of(upStation);
        StationResponse downStationResponse = StationResponse.of(downStation);

        return LineResponse.of(line, upStationResponse, downStationResponse);
    }

    public void updateLine(Long lineId, LineUpdateRequest request) {
        Line line = getLineEntity(lineId);
        line.updateName(request.getName());
        line.updateColor(request.getColor());
    }

    public void deleteLine(Long lineId) {
        Line line = getLineEntity(lineId);
        lineRepository.delete(line);
    }

    private Line getLineEntity(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(
                () -> new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.")
        );
    }

    private Station getStationEntity(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(
                () -> new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.")
        );
    }
}
