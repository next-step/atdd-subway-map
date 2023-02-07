package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.CustomException;
import subway.exception.ErrorCode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()));

        return createLineResponse(line);
    }


    @Transactional
    public LineResponse updateLine(Long id, LineRequest lineRequest) throws CustomException {
        Optional<Line> updateLine = lineRepository.findById(id);

        if (updateLine == null) {
            //throw new NoSuchElementException("Line Id: " + id + " 의 Entity 가 존재 하지 않습니다.");
            throw new CustomException(ErrorCode.LINE_NOT_FOUND);
        } else {
            updateLine.ifPresent(selectLine->{
                updateLine.get().setName(lineRequest.getName());
                updateLine.get().setColor(lineRequest.getColor());
                updateLine.get().setUpStationId(lineRequest.getUpStationId());
                updateLine.get().setDownStationId(lineRequest.getDownStationId());
                updateLine.get().setDistance(lineRequest.getDistance());

                lineRepository.save(updateLine.get());
            });

            return createLineResponse(updateLine.get());
        }
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).get();
        return createLineResponse(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void deleteLines() {
        lineRepository.deleteAll();
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getUpStationId(),
                line.getDownStationId(),
                line.getDistance()
        );
    }
}
