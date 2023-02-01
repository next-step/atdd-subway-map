package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.request.LineRequest;
import subway.line.application.dto.request.LineUpdateRequest;
import subway.line.application.dto.response.LineCreateResponse;
import subway.line.application.dto.response.LineResponse;
import subway.line.domain.Line;
import subway.line.domain.LineQuery;
import subway.line.domain.LineRepository;
import subway.station.domain.Station;
import subway.station.domain.StationQuery;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final LineQuery lineQuery;
    private final StationQuery stationQuery;

    public LineService(final LineRepository lineRepository, final LineQuery lineQuery,
                       final StationQuery stationQuery) {
        this.lineRepository = lineRepository;
        this.lineQuery = lineQuery;
        this.stationQuery = stationQuery;
    }

    public List<LineResponse> findAllLines() {
        return lineQuery.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(final Long lineId) {
        Line line = lineQuery.findById(lineId);

        return LineResponse.from(line);
    }

    @Transactional
    public LineCreateResponse saveLine(final LineRequest lineRequest) {
        Station upStation = stationQuery.findById(lineRequest.getUpStationId());
        Station downStation = stationQuery.findById(lineRequest.getDownStationId());

        Line line = lineRepository.save(lineRequest.toEntity(upStation, downStation));

        return LineCreateResponse.from(line);
    }

    @Transactional
    public void updateLine(final Long lineId, final LineUpdateRequest lineUpdateRequest) {
        Line line = lineQuery.findById(lineId);

        line.updateLine(lineUpdateRequest.toEntity());
    }

    @Transactional
    public void deleteLine(final Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
