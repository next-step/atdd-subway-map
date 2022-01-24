package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.exception.DuplicatedResourceException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private LineVerificationService lineVerificationService;

    public LineService(LineRepository lineRepository, LineVerificationService lineVerificationService) {
        this.lineRepository = lineRepository;
        this.lineVerificationService = lineVerificationService;
    }

    public LineResponse saveLine(LineRequest request) {
        String lineName = request.getName();
        if (lineVerificationService.isExistStationByLineName(lineName)) {
            throw new DuplicatedResourceException(String.format("이미 존재하는 노선이 있습니다. [%s]", lineName));
        }

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(LineResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return LineResponse.fromEntity(lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }

    public void update(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        lineRepository.save(line.update(lineRequest.toEntity()));
    }

    public void delete(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        lineRepository.delete(line);
    }
}
