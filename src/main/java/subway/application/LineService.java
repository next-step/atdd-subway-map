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
import subway.dto.LineRequest;
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

    @Transactional
    public Long save(final LineRequest lineRequest) {
        List<Station> stations = stationRepository.findAllById(lineRequest.getStationIds());
        Line requestSaveLine = lineConverter.lineBy(lineRequest, stations);
        Line line = lineRepository.save(requestSaveLine);
        return line.getId();
    }

    public List<LineResponse> getList() {
        return lineRepository.findAll().stream()
                .map(LineResponse::by)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse get(final Long lineId) {
        Line line = findLineBy(lineId);

        return LineResponse.by(line);
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
