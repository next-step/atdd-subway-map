package nextstep.subway.line.application;

import nextstep.subway.common.exception.InvalidRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
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

    public LineResponse saveLine(LineRequest request) {
        if(isExistLineName(request.getName())) {
            throw new InvalidRequestException("이미 존재하는 노선명이 있습니다.");
        }
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> getLineList() {
        List<Line> lineList = lineRepository.findAll();
        return lineList.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    public boolean isExistLineName(String name) {
        return lineRepository.existsLineByName(name);
    }

    public LineResponse getLine(Long id) {
        return LineResponse.of(lineRepository.findById(id).orElseThrow(() -> new InvalidRequestException("라인이 존재하지 않습니다.")));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line addedLine = lineRepository.getOne(id);
        if(addedLine == null) {
            throw new InvalidRequestException("업데이트 할 라인이 등록되어 있지 않습니다.");
        }
        addedLine.update(lineRequest.toLine());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
