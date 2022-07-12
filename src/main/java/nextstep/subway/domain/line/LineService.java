package nextstep.subway.domain.line;

import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.ui.dto.line.CreateLineRequest;
import nextstep.subway.ui.dto.line.LineResponse;
import nextstep.subway.ui.dto.line.UpdateLineRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {
    public static final String LINE_NOT_FOUND = "노선이 존재하지 않습니다.";
    public static final String STATION_NOT_FOUND = "지하철역이 존재하지 않습니다.";

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(CreateLineRequest request) {
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());

        Line line = new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
        lineRepository.save(line);

        return LineResponse.of(line, List.of(upStation, downStation));
    }

    public List<LineResponse> findAllLine() {
        return lineRepository.findAll().stream()
                             .map(line -> LineResponse.of(line, List.of(line.getUpStation(), line.getDownStation())))
                             .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long lineId) {
        Line line = findLine(lineId);
        List<Station> stations = List.of(line.getUpStation(), line.getDownStation());
        return LineResponse.of(line, stations);
    }

    public void updateLine(Long lineId, UpdateLineRequest request) {
        Line line = findLine(lineId);
        line.update(request.getName(), request.getColor());
    }

    public void deleteLine(final Long lineId) {
        lineRepository.deleteById(lineId);
    }

    private Station findStation(long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(() -> new IllegalArgumentException(STATION_NOT_FOUND));
    }

    private Line findLine(final Long lineId) {
        return lineRepository.findById(lineId)
                             .orElseThrow(() -> new IllegalArgumentException(LINE_NOT_FOUND));
    }
}
