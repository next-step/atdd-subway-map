package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.line.CreateLineRequest;
import nextstep.subway.applicaion.dto.line.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {
    public static final int STATION_IN_LINE_INIT_NUM = 2;

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveStationLine(CreateLineRequest request) {
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
}
