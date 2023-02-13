package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.dtos.request.LineRequest;
import subway.dtos.response.LineResponse;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private EntityManager entityManager;

    public LineService(LineRepository lineRepository, EntityManager entityManager){
        this.lineRepository = lineRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = entityManager.find(Station.class, lineRequest.getUpStationId());
        Station downStation = entityManager.find(Station.class, lineRequest.getDownStationId());
        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                upStation,
                downStation
                ));
        return createLineResponse(line);
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
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();
        line.updateLine(lineRequest.getName(), lineRequest.getColor());
        lineRepository.save(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line){
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations()
        );
    }
}
