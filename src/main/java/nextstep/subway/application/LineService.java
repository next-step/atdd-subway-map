package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.application.dto.SectionResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {
    private static final String LINE_NAME_IS_ALREADY_REGISTERED = "이미 등록된 노선명입니다.";
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public LineResponse save(LineRequest request) {
        validate(request.getName());

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));

        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(NoSuchElementException::new);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(NoSuchElementException::new);
        sectionRepository.save(new Section(line, upStation, downStation));

        return createLineResponse(line, List.of(upStation, downStation));
    }

    private void validate(String lineName) {
        if (lineRepository.existsByName(lineName)) {
            throw new IllegalArgumentException(LINE_NAME_IS_ALREADY_REGISTERED);
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(line -> createLineResponse(line, line.getStations()))
                .collect(Collectors.toList());

    }

    private LineResponse createLineResponse(Line line, List<Station> stations) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stations)
                .createdDate(line.getCreatedDate())
                .modifiedDate(line.getModifiedDate())
                .build();
    }

    public LineResponse findBy(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return this.createLineResponse(line, line.getStations());
    }

    public void updateBy(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
        line.update(request.getName(), request.getColor());
    }

    public void deleteBy(Long id) {
        lineRepository.deleteById(id);
    }

    public SectionResponse addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(NoSuchElementException::new);
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(NoSuchElementException::new);
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(NoSuchElementException::new);
        Section section = sectionRepository.save(new Section(line, upStation, downStation));
        return createSectionResponse(section);
    }

    // TODO: response로 메서드 이동
    private SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getUpStationId(),
                section.getDownStationId(),
                section.getDistance(),
                section.getCreatedDate(),
                section.getModifiedDate());
    }
}
