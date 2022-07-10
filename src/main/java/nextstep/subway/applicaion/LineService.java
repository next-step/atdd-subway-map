package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.mapper.domain.LineMapper;
import nextstep.subway.applicaion.mapper.response.LineResponseMapper;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final LineMapper lineMapper;
    private final LineResponseMapper lineResponseMapper;

    public LineService(LineRepository lineRepository, LineMapper lineMapper, LineResponseMapper lineResponseMapper) {
        this.lineRepository = lineRepository;
        this.lineMapper = lineMapper;
        this.lineResponseMapper = lineResponseMapper;
    }

    @Transactional
    public LineResponse createLine(LineRequest lineRequest) {
        Line line = lineRepository.save(lineMapper.map(lineRequest));
        return lineResponseMapper.map(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(lineResponseMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id)
                .map(lineResponseMapper::map)
                .orElseThrow(() -> new NotFoundException(id + "번 노선을 찾지 못했습니다."));
    }

    @Transactional
    public void modifyLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id + "번 노선을 찾지 못했습니다."));

        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
