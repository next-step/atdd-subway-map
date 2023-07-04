package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly =true)
public class LineService {

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 역입니다."));
        stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 역입니다."));

        Line line = lineRepository.save(new Line(lineRequest.getId(),lineRequest.getColor(),lineRequest.getName(),lineRequest.getUpStationId(),lineRequest.getDownStationId(),lineRequest.getDistance()));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public LineResponse updateLine(LineRequest lineRequest) {
        Line line = lineRepository.findById(lineRequest.getId()).get();
        if(lineRequest.getName() != null) line.setName(lineRequest.getName());
        if(lineRequest.getColor() != null) line.setColor(lineRequest.getColor());
        if(lineRequest.getUpStationId() != null) line.setUpStationId(lineRequest.getUpStationId());
        if(lineRequest.getDownStationId() != null) line.setDownStationId(lineRequest.getDownStationId());
        if(lineRequest.getDistance() != 0) line.setDistance(lineRequest.getDistance());

        return createLineResponse(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(),line.getColor(), line.getName(),line.getUpStationId(),line.getDownStationId(),line.getDistance());
    }

}
