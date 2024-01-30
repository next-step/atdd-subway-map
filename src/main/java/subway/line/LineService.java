package subway.line;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(
        LineRepository lineRepository,
        StationRepository stationRepository
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    /** 지하철 노선을 생성한다. */
    @Transactional
    public LineResponse createLine(LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId())
            .orElseThrow();
        Station downStation = stationRepository.findById(request.getDownStationId())
            .orElseThrow();

        Line line = lineRepository.save(
            new Line(request.getName(), request.getColor(), request.getDistance(), upStation, downStation)
        );

        return new LineResponse(line);
    }

    /** 지하철 노선 목록을 조회한다. */
    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                    .map(LineResponse::new)
                    .collect(Collectors.toList());
    }

    /** 지하철 노선을 조회한다. */
    public LineResponse getLine(
        Long id
    ) {
        Line line = lineRepository.findById(id).orElseThrow();
        return new LineResponse(line);
    }

    /** 지하철 노선을 수정한다. */
    @Transactional
    public void modifyLine(
        Long id,
        LineUpdateRequest request
    ) {
        Line line = lineRepository.findById(id).orElseThrow();
        line.updateLine(request);
    }

    /** 지하철 노선을 삭제한다. */
    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
