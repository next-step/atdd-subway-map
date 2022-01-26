package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.BadRequestException;
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
    private final StationService stationService;

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
            throw new BadRequestException("중복된 노선 이름입니다.");
        }
    }

    private Section createSection(LineRequest request, Line line) {
        Station upStation = stationService.findById(request.getUpStationId(), "상행역이 존재하지 않습니다");
        Station downStation = stationService.findById(request.getDownStationId(), "하행역이 존재하지 않습니다.");

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

    private LineResponse createLineResponse(Line line) {
        Sections sections = new Sections(line.getSections());
        List<StationResponse> stations = sections.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stations)
                .createdDate(line.getCreatedDate())
                .modifiedDate(line.getModifiedDate())
                .build();
    }

    public LineResponse findLine(Long id) {
        Line line = findById(id);
        return createLineResponse(line);
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = findById(id);
        line.update(request.getName(), request.getColor());
        return createLineResponse(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 노선입니다."));
    }

}
