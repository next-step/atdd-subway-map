package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
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

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        try {
            Line persistLine = lineRepository.save(request.toLine());
            return LineResponse.of(persistLine);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateLineException("이미 등록한 라인 입니다.");
        }

    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(line -> LineResponse.of(line)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(final Long lineId) {
        Optional<Line> line = lineRepository.findById(lineId);
        return line.map(LineResponse::of)
                .orElseThrow(() -> new NoSuchLineException("Not found lineId"+lineId));
    }

    public void updateLine(final Long lineId, LineRequest lineRequest) {
        Optional< Line > optionalLine = lineRepository.findById(lineId);
        if(!optionalLine.isPresent()) {
            throw new NoSuchLineException("해당하는 라인이 없습니다.");
        }
        optionalLine.get().update(lineRequest.toLine());
    }

    public void deleteLine(final Long lineId) {
        Optional<Line> line = lineRepository.findById(lineId);
        if(line.isPresent()){
            lineRepository.delete(line.get());
        }
    }
}
