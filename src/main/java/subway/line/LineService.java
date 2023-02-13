package subway.line;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.CustomException;
import subway.exception.ErrorDto;

import java.util.List;
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
//        Line line = lineRepository.save(new Line(
//                lineRequest.getName(),
//                lineRequest.getColor(),
//                lineRequest.getUpStationId(),
//                lineRequest.getDownStationId(),
//                lineRequest.getDistance()));

        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor()));

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest lineRequest) throws CustomException {
        Line line = findLineById(id);
        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());
        line.setUpStationId(lineRequest.getUpStationId());
        line.setDownStationId(lineRequest.getDownStationId());
        line.setDistance(lineRequest.getDistance());
        lineRepository.save(line);
        return createLineResponse(line);
    }


    public Line findLineById(Long id) throws CustomException {
        return lineRepository.findById(id).orElseThrow(()->new CustomException(
                new ErrorDto(HttpStatus.NOT_FOUND, "지하철노선 Id("+ id +") 가 존재 하지 않습니다.")));
    }

    public LineResponse findLineResponseById(Long id) throws CustomException {
        return createLineResponse(findLineById(id));
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
                line.getColor()
        );
    }
}
