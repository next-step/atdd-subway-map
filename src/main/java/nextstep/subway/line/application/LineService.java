package nextstep.subway.line.application;

import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.applicaion.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse createLine(final LineRequest lineRequest) {
        final Line savedLine = lineRepository.save(lineRequest.toLine());
        return createLineResponse(savedLine);
    }

    @Transactional
    public void modifyLine(final Long id, final LineRequest lineRequest) {
        lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 노선이 존재하지 않습니다."));

        lineRepository.save(lineRequest.toLine(id));
    }

    private LineResponse createLineResponse(final Line savedLine) {
        return LineResponse.builder()
                .id(savedLine.getId())
                .name(savedLine.getName())
                .color(savedLine.getColor())
                .stations(stationService.findStations(Arrays.asList(savedLine.getUpStationId(), savedLine.getDownStationId()))).build();
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }
}
