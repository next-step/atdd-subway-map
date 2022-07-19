package nextstep.subway.applicaion.line;

import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.LineResponse;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.line.domain.LineRepository;
import nextstep.subway.applicaion.station.domain.Station;
import nextstep.subway.applicaion.station.domain.StationRepository;
import nextstep.subway.applicaion.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        stationRepository.save(new Station(lineRequest.getUpStationId()));
        stationRepository.save(new Station(lineRequest.getDownStationId()));
        Line line = lineRepository.save(new Line(lineRequest));
        return createLineResponse(line);
    }

    @Transactional
    public LineResponse updateLine(LineRequest lineRequest) {
        Line line = lineRepository.getById(lineRequest.getId());
        line.update(lineRequest.getName(), lineRequest.getColor());
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                getStations(line)
        );
    }

    private List<StationResponse> getStations(Line line) {
        Station upStation = stationRepository.getById(line.getUpStationId());
        Station downStation = stationRepository.getById(line.getDownStationId());
        return List.of(StationResponse.from(upStation), StationResponse.from(downStation));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse findById(Long id) {
        Optional<Line> line = lineRepository.findById(id);
        return createLineResponse(line.get());
    }
}
