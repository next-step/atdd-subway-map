package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.responseconverter.LineResponseConverter;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.EntityNotFoundException;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final LineResponseConverter lineResponseConverter;

    public LineService(LineRepository lineRepository, LineResponseConverter lineResponseConverter) {
        this.lineRepository = lineRepository;
        this.lineResponseConverter = lineResponseConverter;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return lineResponseConverter.toResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                             .stream()
                             .map(lineResponseConverter::toResponse)
                             .collect(Collectors.toList());
    }

    public LineResponse findById(long id) {
        return lineRepository.findById(id)
                             .map(lineResponseConverter::toResponse)
                             .orElseThrow(EntityNotFoundException::new);
    }

    public void edit(long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
        line.edit(lineRequest.getName(), lineRequest.getColor());
    }

    public void delete(long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
        lineRepository.delete(line);
    }
}
