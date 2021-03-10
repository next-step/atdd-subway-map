package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineDomainService;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.LineCreateFailException;
import nextstep.subway.line.exception.LineNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.OperationNotSupportedException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private LineDomainService lineDomainService;
    private SectionService sectionService;
    private LineRepository lineRepository;

    public LineService(LineDomainService lineDomainService, SectionService sectionService, LineRepository lineRepository) {
        this.lineDomainService = lineDomainService;
        this.sectionService = sectionService;
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = request.toLine();

        boolean alreadyRegistered = lineRepository.findByName(line.getName()).isPresent();
        if (alreadyRegistered) {
            throw LineCreateFailException.alreadyExist();
        }

        Line savedLine = lineRepository.save(line);
        sectionService.addSection(savedLine.getId(), request.toSectionRequest());

        return LineResponse.of(savedLine);
    }

    public List<LineResponse> getAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long lineId) {
        return LineResponse.of(lineDomainService.getLineEntity(lineId));
    }

    public void updateLine(Long lineId, LineRequest lineRequest) {
        Line line = lineDomainService.getLineEntity(lineId);
        line.update(lineRequest.toLine());
    }

    public void deleteLine(Long lineId) {
        Line line = lineDomainService.getLineEntity(lineId);
        lineRepository.delete(line);
    }



}
