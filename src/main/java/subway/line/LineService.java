package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;

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
    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Optional<Line> updateLine = lineRepository.findById(id);

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

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).get();
        //if (line != null) {
        return createLineResponse(line);
        //} else {return null;}
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
