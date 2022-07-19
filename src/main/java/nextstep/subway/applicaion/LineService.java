package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveStationLine(LineRequest lineRequest) {

        Line savedLine = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));
        return new LineResponse(
                savedLine.getId(), savedLine.getName(), savedLine.getColor(), findStationsRelatedLine(savedLine)
        );
    }

    private List<StationResponse> findStationsRelatedLine(Line savedLine) {
        return stationRepository.findAllById(List.of(savedLine.getUpStationId(), savedLine.getDownStationId()))
                                .stream()
                                .map(StationResponse::of)
                                .collect(Collectors.toList());
    }

    public List<LineResponse> findAllStationsLines() {
        return lineRepository.findAll()
                            .stream()
                            .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor(), findStationsRelatedLine(line)))
                            .collect(Collectors.toList());
    }

    public LineResponse findStationLine(Long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("해당 호선은 없습니다."));
        return new LineResponse(line.getId(), line.getName(), line.getColor(), findStationsRelatedLine(line));
    }

    @Transactional
    public void updateStationLine(LineUpdateRequest request, Long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("해당 호선은 업습니다"));

        line.updateNameAndColor(request.getName(), request.getColor());
    }

    public void deleteStationLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 호선은 없습니다."));
        lineRepository.deleteById(line.getId());
    }

    public LineResponse createStationSection(Long id, SectionRequest sectionRequest) {

    }
}
