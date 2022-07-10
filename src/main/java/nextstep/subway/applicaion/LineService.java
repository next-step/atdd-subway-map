package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.line.CreateLineRequest;
import nextstep.subway.applicaion.dto.line.LineResponse;
import nextstep.subway.applicaion.dto.line.UpdateLineRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(CreateLineRequest request) {
        List<Station> stations = stationRepository.findAllById(List.of(request.getUpStationId(), request.getDownStationId()));

        Line line = request.toEntity();
        lineRepository.save(line);

        return LineResponse.of(line, stations);
    }

    public List<LineResponse> findAllLine() {
        return lineRepository.findAll().stream()
                             .map(line -> {
                                 List<Station> stations = stationRepository.findAllById(List.of(line.getUpStationId(),
                                                                                                line.getDownStationId()));
                                 return LineResponse.of(line, stations);
                             }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long lineId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
        List<Station> stations = stationRepository.findAllById(List.of(line.getUpStationId(), line.getDownStationId()));
        return LineResponse.of(line, stations);
    }

    public void updateLine(Long lineId, UpdateLineRequest request){
        Line line = lineRepository.findById(lineId)
                                  .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
        line.setName(StringUtils.hasText(request.getName()) ? request.getName() : line.getName());
        line.setColor(StringUtils.hasText(request.getColor()) ? request.getColor() : line.getColor());
    }
}
