package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    LineRepository lineRepository;
    StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines(){
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        List<Station> stationList = new ArrayList<>();
        stationList.add(stationService.findById(line.getUpStationId()));
        stationList.add(stationService.findById(line.getDownStationId()));

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationList);
    }

    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id).map(this::createLineResponse).get();
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();

        if (! line.getName().equals(lineRequest.getName())) {
            line.changeName(lineRequest.getName());
        }

        if (! line.getColor().equals(lineRequest.getColor())) {
            line.changeColor(lineRequest.getColor());
        }

        lineRepository.save(line);
    }
}
