package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.LineUpdateRequest;
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
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {
        stationRepository.findById(lineCreateRequest.getUpStationId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 역입니다."));
        stationRepository.findById(lineCreateRequest.getDownStationId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 역입니다."));

        Line line = lineRepository.save(Line.builder()
                                                .name(lineCreateRequest.getName())
                                                .color(lineCreateRequest.getColor())
                                                .upStationId(lineCreateRequest.getUpStationId())
                                                .downStationId(lineCreateRequest.getDownStationId())
                                                .distance(lineCreateRequest.getDistance())
                                                .build());
        return new LineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse :: new)
                .collect(Collectors.toList());
    }


    @Transactional
    public LineResponse updateLine(LineUpdateRequest lineCreateRequest) {
        Line line = lineRepository.findById(lineCreateRequest.getId()).get();
        line.setName(lineCreateRequest.getName());
        line.setColor(lineCreateRequest.getColor());

        return new LineResponse(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

}
