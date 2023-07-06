package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineSectionRegisterRequest;
import subway.dto.LineUpdateRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException());

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getContainStations()
                , line.getDistance()
        );
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(() -> new RuntimeException());
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(() -> new RuntimeException());
        Section section = sectionRepository.save(new Section(upStation, downStation, lineRequest.getDistance()));

        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                section
        ));

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Line updateLineById(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException());
        line.updateNameAndColor(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        return line;
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getContainStations(),
                line.getDistance()
        );
    }

    public void registerSections(Long id, LineSectionRegisterRequest lineSectionRegisterRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException());
        Station downStation= stationRepository.findById(lineSectionRegisterRequest.getDownStationId()).orElseThrow(() -> new RuntimeException());
        Station upStation = stationRepository.findById(lineSectionRegisterRequest.getUpStationId()).orElseThrow(() -> new RuntimeException());

        Section section = new Section(upStation, downStation, lineSectionRegisterRequest.getDistance());
        line.registerSection(section);
    }
}
