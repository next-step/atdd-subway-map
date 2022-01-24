package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.LineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineResponse saveLine(LineRequest request) {
        validateDuplicateLineName(request.getName());

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        Section section = createSection(request, line);
        line.getSections().add(section);

        StationResponse upStation = StationResponse.of(section.getUpStation());
        StationResponse downStation = StationResponse.of(section.getDownStation());

        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(Arrays.asList(upStation, downStation))
                .createdDate(line.getCreatedDate())
                .modifiedDate(line.getModifiedDate())
                .build();
    }

    private void validateDuplicateLineName(String name) {
        if(lineRepository.findByName(name) != null) {
            throw new RuntimeException("중복된 이름입니다.");
        }
    }

    private Section createSection(LineRequest request, Line line) {
        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new RuntimeException("상행역을 입력해주세요"));
        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new RuntimeException("하행역을 입력해주세요"));

        return Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException("해당 노선은 존재하지 않습니다."));

        return createLineResponse(line);
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException("해당 노선은 존재하지 않습니다."));

        line.update(request.getName(), request.getColor());
        return createLineResponse(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }
}
