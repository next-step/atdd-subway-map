package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.line.exception.DuplicateLineException;
import nextstep.subway.line.exception.NoSuchLineException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        try {
            Line newLine = request.toLine();
            newLine.addSection(request.toSection());
            Line persistLine = lineRepository.save(newLine);
            return LineResponse.of(persistLine);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateLineException("Duplicated Line");
        }
    }

    public List<LineResponse> getAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(line -> LineResponse.of(line)).collect(Collectors.toList());
    }

    public LineResponse getLineById(final Long lineId) {
        Optional<Line> line = lineRepository.findById(lineId);
        return line.map(LineResponse::of)
                .orElseThrow(() -> new IllegalArgumentException("Not found lineId"+lineId));
    }

    public void updateLine(final Long lineId, LineRequest lineRequest) {
        Optional< Line > optionalLine = lineRepository.findById(lineId);
        if(!optionalLine.isPresent()) {
            throw new NoSuchLineException("No such line");
        }
        optionalLine.get().update(lineRequest.toLine());
    }

    public void deleteLine(final Long lineId) {
        Optional<Line> line = lineRepository.findById(lineId);
        if(line.isPresent()){
            lineRepository.delete(line.get());
        }
    }

    public SectionResponse saveSection(final Long lineId, SectionRequest sectionRequest) {
        Optional<Line> optionalLine = lineRepository.findById(lineId);
        if(!optionalLine.isPresent()){
            throw new NoSuchLineException("No such line");
        }
        Line line = optionalLine.get();
        line.addSection(sectionRequest.toSection());
        return new SectionResponse();
    }
}
