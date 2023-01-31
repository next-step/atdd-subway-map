package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(
                new Line(lineRequest.getName(), lineRequest.getUpStationId(), lineRequest.getDownStationId())
        );
        return createLineResponse(line);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public LineResponse patchLine(Long id, LineRequest lineRequest) {
        validPatchLineRequest(lineRequest);

        Line line = getById(id);
        line.patch(lineRequest);

        return createLineResponse(lineRepository.save(line));
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        return new LineResponse(getById(id));
    }

    private Line getById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("노선 정보가 없습니다."));
    }

    private void validPatchLineRequest(LineRequest lineRequest) {
        if (lineRequest.getName() == null || lineRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("노선 이름은 필수 입니다.");
        }
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line);
    }

}
