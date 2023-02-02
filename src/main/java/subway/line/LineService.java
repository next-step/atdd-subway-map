package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.getStationById(lineRequest.getUpStationId());
        Station downStation = stationService.getStationById(lineRequest.getDownStationId());

        Line line = lineRequest.toEntity(upStation, downStation);
        Line savedLine = lineRepository.save(line);

        return LineResponse.of(savedLine);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    /**
     * LineResponse를 반환하는 메서드
     *
     * @param lineId
     * @return LineResponse
     */
    public LineResponse findLineById(long lineId) {
        Line line = getLineById(lineId);

        return LineResponse.of(line);
    }

    /**
     * Line 엔티티를 반환하는 메서드
     *
     * @param lineId
     * @return Line 엔티티
     * @throws LineNotFoundException 존재하지 않는 id 조회시
     */
    public Line getLineById(long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public void updateLine(long lineId, LineRequest lineRequest) {
        Line line = getLineById(lineId);

        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(long lineId) {
        lineRepository.deleteById(lineId);
    }
}
