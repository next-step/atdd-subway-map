package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line createLine(LineCreateRequest request) {
        Line line = Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .upStationId(request.getUpStationId())
                .downStationId(request.getDownStationId())
                .distance(request.getDistance())
                .build();
        return lineRepository.save(line);

    }


    public Line readLine(Long id) throws HttpException {
        return lineRepository.findById(id).orElseThrow(() -> new HttpException(ErrorCode.MISSING_ID));
    }


    public List<Line> readLines() {
        return lineRepository.findAll();
    }

    @Transactional
    public void updateLine(LineUpdateDTO dto) throws HttpException {
        Line line = lineRepository.findById(dto.getId())
                .orElseThrow(() -> new HttpException(ErrorCode.MISSING_ID));
        line.changeColor(dto.getColor());
        line.changeName(dto.getName());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
