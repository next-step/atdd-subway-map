package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(StationRepository stationRepository, LineRepository lineRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(),
                stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(), lineRequest.getDistance());
        return createLineResponse(lineRepository.save(line));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return createLineResponse(lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("지하철 노선 데이터가 없습니다.")));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("지하철 노선 데이터가 없습니다.")).update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getUpStation(), line.getDownStation(), line.getDistance());
    }

}
