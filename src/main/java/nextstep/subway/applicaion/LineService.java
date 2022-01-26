package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.LogicError;
import nextstep.subway.exception.LogicException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {

        if(isExistLineName(lineRequest.getName())){
            throw new LogicException(LogicError.DUPLICATED_NAME_LINE);
        }

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        return LineResponse.of(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = findById(id);
        return LineResponse.of(line);
    }

    public LineResponse modifyLine(Long id, LineRequest lineRequest) {
        findById(id);
        Line line = lineRequest.toEntity(id);
        Line modifiedLine = lineRepository.save(line);
        return LineResponse.of(modifiedLine);
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LogicException(LogicError.NOT_EXIST_LINE));
    }

    private boolean isExistLineName(String name) {
        return lineRepository.findByName(name).isPresent();
    }
}
