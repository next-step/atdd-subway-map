package subway.application;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.converter.LineConverter;
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
        List<Station> stations = stationRepository.findAllById(line.getStationIds());
        line.validateStationSize(stations.size());
        return LineResponse.by(line, StationResponse.by(stations));
    }

    public List<LineResponse> getList() {
        List<Line> lines = lineRepository.findAll();
        List<LineResponse> result = new ArrayList<>();
        for (Line line : lines) {
            List<Station> stations = stationRepository.findAllById(line.getStationIds());
            line.validateStationSize(stations.size());
            result.add(LineResponse.by(line, StationResponse.by(stations)));
        }
        return result;
    }

    @Transactional
    public Long save(final LineCreateRequest lineCreateRequest) {
        List<Station> stations = stationRepository
                .findAllById(List.of(lineCreateRequest.getUpStationId(), lineCreateRequest.getDownStationId()));
        Line line = lineConverter.lineBy(lineCreateRequest);
        line.validateStationSize(stations.size());
        lineRepository.save(line);
        return line.getId();
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
