package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.domain.repository.StationRepository;
import subway.dtos.request.LineRequest;
import subway.dtos.response.LineResponse;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository){
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(NoSuchElementException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(NoSuchElementException::new);
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
    public void updateLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
        line.updateLine(lineRequest.getName(), lineRequest.getColor());
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
