package nextstep.subway.applicaion;

import javassist.bytecode.DuplicateMemberException;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.DuplicatedLineException;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        checkDuplication(request);
        Line line = lineRepository.save(Line.of(request));
        return LineResponse.of(line);
    }

    private void checkDuplication(LineRequest request) {
        if (lineRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicatedLineException();
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineResponse.of(line)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        return lineRepository.findById(id)
                .map(line -> LineResponse.of(line)).orElseThrow(() -> new EntityNotFoundException());
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        line.update(lineRequest.getName(), lineRequest.getColor());
        return LineResponse.of(line);
    }

    public void deleteLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        lineRepository.delete(line);
    }
}
