package nextstep.subway.applicaion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station downStation = getStation(lineRequest.getDownStationId());
        Station upStation = getStation(lineRequest.getUpStationId());

        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getDistance(),
                upStation, downStation);
        return createLineResponse(lineRepository.save(line));
    }

    public LineResponse findLine(Long lineId) {
        return createLineResponse(getLine(lineId));
    }

    private Line getLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("해당 노선이 없습니다."));
    }

    private Station getStation(Long upStationId) {
        return stationRepository.findById(upStationId)
                .orElseThrow(() -> new EntityNotFoundException("해당 역이 없습니다."));
    }

    private LineResponse createLineResponse(Line line) {
        List<StationResponse> stationResponses = createStationResponses(line.getDownStation(), line.getUpStation());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

    private List<StationResponse> createStationResponses(Station downStation, Station upStation) {
        return Arrays.asList(
                createStationResponse(downStation),
                createStationResponse(upStation)
        );
    }
}
