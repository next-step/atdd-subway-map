package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineQueryService {

    private static final String LINE_NOTFOUND_MESSAGE = "해당 id의 지하철 노선이 존재하지 않습니다.";

    private final StationQueryService stationQueryService;
    private final LineRepository lineRepository;

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return createLineResponse(findLineOrElseThrow(id));
    }

    private Line findLineOrElseThrow(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(LINE_NOTFOUND_MESSAGE));
    }

    private LineResponse createLineResponse(Line line) {
        List<StationResponse> stations = stationQueryService.findAllStations();
        return new LineResponse(line, stations);
    }

}
