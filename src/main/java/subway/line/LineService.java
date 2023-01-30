package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.exception.station.StationNotFoundExceptionException;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(final LineCreateRequest lineCreateRequest) {
        final Station upStation = getStationByStationId(lineCreateRequest.getUpStationId());
        final Station downStation = getStationByStationId(lineCreateRequest.getDownStationId());

        final Line line = Line.of(lineCreateRequest, upStation, downStation);
        lineRepository.save(line);

        return LineResponse.from(line);
    }

    private Station getStationByStationId(final Long lineCreateRequest) {
        return stationRepository.findById(lineCreateRequest)
                .orElseThrow(StationNotFoundExceptionException::new);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(final Long id) {
        final Line line = lineRepository.getLine(id);
        return LineResponse.from(line);
    }

    public void updateLine(final Long id, LineUpdateRequest lineUpdateRequest) {
        final Line line = lineRepository.getLine(id);
        line.update(lineUpdateRequest);
    }

    public void deleteLine(final Long id) {
        final Line line = lineRepository.getLine(id);
        lineRepository.delete(line);
    }
}
