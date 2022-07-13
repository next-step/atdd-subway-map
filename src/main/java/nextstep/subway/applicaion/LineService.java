package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateDto;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));
        LineResponse lineResponse = createLineResponse(line);

        return lineResponse;
    }

    private LineResponse createLineResponse(Line line) {
        List<StationResponse> stationResponses = new ArrayList<>();

        stationResponses.add(stationService.createStationResponse(line.getUpStationId()));
        stationResponses.add(stationService.createStationResponse(line.getDownStationId()));

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stationResponses
        );
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public Line findLine(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException(lineId + "번 id로 조회되는 노선이 없습니다."));
    }

    @Transactional
    public void updateLine(Long id, LineUpdateDto updateDto) {
        Line line = findLine(id);
        line.updateLine(updateDto);

        lineRepository.save(line);
    }
}
