package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.request.LineCreateRequest;
import subway.dto.request.LineUpdateRequest;
import subway.dto.response.LineResponse;
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
        Station upStation = stationRepository.findById(lineCreateRequest.getUpStationId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        Station downStation = stationRepository.findById(lineCreateRequest.getDownStationId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        Line line = lineRepository.save(new Line(lineCreateRequest.getColor(),lineCreateRequest.getName()));

        line.getSections().add(Section.builder()
                                        .line(line)
                                        .upStation(upStation)
                                        .downStation(downStation)
                                        .distance(line.getDistance())
                                        .build());

        return new LineResponse(line);
    }

    public List<LineResponse> findAllLineResponse() {
        return lineRepository.findAll().stream()
                .map(LineResponse::createLineResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public LineResponse updateLine(LineUpdateRequest lineCreateRequest) {
        Line line = lineRepository.findById(lineCreateRequest.getId()).get();
        line.changeName(lineCreateRequest.getName());
        line.changeColor(lineCreateRequest.getColor());

        return LineResponse.createLineResponse(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
