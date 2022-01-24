package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.DuplicateRegistrationRequestException;
import nextstep.subway.exception.NotFoundRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) throws DuplicateRegistrationRequestException {
        Line findLine = lineRepository.findByName(request.getName());
        if (ObjectUtils.isEmpty(findLine)) {
            Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
            return LineResponse.createLineResponse(line);
        }

        throw new DuplicateRegistrationRequestException("이미 등록된 노선입니다. 노선 이름 = " + request.getName());
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) throws NotFoundRequestException {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundRequestException("존재하지 않는 노선입니다. id = " + id));

        return LineResponse.createLineResponse(line);
    }

    public void updateLineById(Long id, LineRequest lineRequest) throws NotFoundRequestException {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundRequestException("존재하지 않는 노선입니다. id = " + id));

        line.update(lineRequest.getName(), lineRequest.getColor());
        LineResponse.createLineResponse(line);
    }

    public void deleteLineById(Long id) throws NotFoundRequestException {
        lineRepository.deleteById(id);
    }
}
