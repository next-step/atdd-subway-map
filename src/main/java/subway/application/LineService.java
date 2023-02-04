package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.LineCreateRequest;
import subway.dto.LineEditRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(
            final LineRepository lineRepository,
            final StationRepository stationRepository
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse getBy(final Long lineId) {
        Line line = lineRepository.findByIdWithStation(lineId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노선입니다."));
        return LineResponse.by(line, StationResponse.by(line.getStations()));
    }

    public List<LineResponse> getList() {
        return lineRepository.findAllWithStation().stream()
                .map(line -> LineResponse.by(line, StationResponse.by(line.getStations())))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Long save(final LineCreateRequest lineCreateRequest) {
        Line line = convertToLineBy(lineCreateRequest);
        lineRepository.save(line);
        return line.getId();
    }

    private Line convertToLineBy(final LineCreateRequest lineCreateRequest) {
        Station upStation = getStationBy(lineCreateRequest.getUpStationId());
        Station downStation = getStationBy(lineCreateRequest.getDownStationId());
        return new Line(
                lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                upStation,
                downStation,
                lineCreateRequest.getDistance()
        );
    }

    private Station getStationBy(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지하철 역 입니다."));
    }

    @Transactional
    public void edit(final Long lineId, final LineEditRequest lineEditRequest) {
        Line line = findLineBy(lineId);
        line.modify(lineEditRequest.getName(), lineEditRequest.getColor(), lineEditRequest.getDistance());
    }

    @Transactional
    public void delete(final Long lineId) {
        Line line = findLineBy(lineId);
        lineRepository.delete(line);
    }

    private Line findLineBy(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노선입니다."));
    }
}
