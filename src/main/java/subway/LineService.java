package subway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static subway.LineMapper.LINE_MAPPER;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;

    public LineResponse createLine(AddLineRequest addLineRequest) {
        Line line = LINE_MAPPER.mapToLine(addLineRequest);
        Line savedLine = lineRepository.save(line);
        return LINE_MAPPER.mapToLineResponse(savedLine);
    }

    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LINE_MAPPER::mapToLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = findLineById(id);
        return LINE_MAPPER.mapToLineResponse(line);
    }

    public ModifyLineResponse modifyLine(Long id, ModifyLineRequest modifyLineRequest) {
        Line line = findLineById(id);
        line.setName(modifyLineRequest.getName());
        Line modifiedLine = lineRepository.save(line);
        return LINE_MAPPER.mapToModifyLineResponse(modifiedLine);
    }

    public void deleteLine(Long id) {
        if (!lineRepository.existsById(id)) {
            throw new LineNotFoundException(id);
        }
        lineRepository.deleteById(id);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException(id));
    }
}
