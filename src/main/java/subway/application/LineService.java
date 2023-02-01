package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.converter.LineConverter;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.LineEditRequest;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final LineConverter lineConverter;
    private final StationRepository stationRepository;

    public LineService(
            final LineRepository lineRepository,
            final LineConverter lineConverter,
            final StationRepository stationRepository
    ) {
        this.lineRepository = lineRepository;
        this.lineConverter = lineConverter;
        this.stationRepository = stationRepository;
    }

    public LineResponse getBy(final Long lineId) {
        Line line = findLineBy(lineId);
        return LineResponse.by(line);
    }

    public List<LineResponse> getList() {
        return lineRepository.findAll().stream()
                .map(LineResponse::by)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Long save(final LineCreateRequest lineCreateRequest) {
        List<Station> stations = stationRepository
                .findAllById(List.of(lineCreateRequest.getUpStationId(), lineCreateRequest.getDownStationId()));
        Line line = lineConverter.lineBy(lineCreateRequest, stations);
        lineRepository.save(line);
        return line.getId();
    }

    @Transactional
    public void edit(final Long lineId, final LineEditRequest lineEditRequest) {
        Line line = findLineBy(lineId);
        line.editName(lineEditRequest.getName())
                .editColor(lineEditRequest.getColor())
                .editDistance(lineEditRequest.getDistance());
    }

    @Transactional
    public void delete(final Long lineId) {
        Line line = findLineBy(lineId);
        line.detachStations();
        lineRepository.delete(line);
    }

    private Line findLineBy(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노선입니다."));
    }
}
