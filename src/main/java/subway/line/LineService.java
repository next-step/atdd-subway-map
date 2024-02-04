package subway.line;

import org.springframework.stereotype.Service;
import subway.line.section.*;
import subway.station.Station;
import subway.station.StationNotFoundException;
import subway.station.StationRepository;
import subway.station.StationResponse;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getSections().allStations());
    }

    public LineResponse createLine(LineRequest lineRequest) throws StationNotFoundException, CannotAddSectionException {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(StationNotFoundException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(StationNotFoundException::new);

        Line line = new Line(lineRequest.getName(), lineRequest.getColor());
        line.addSection(new Section(line, upStation, downStation, lineRequest.getDistance()));

        lineRepository.save(line);
        return createLineResponse(line);
    }

    public List<LineResponse> showLines() {
        return lineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse showLine(Long id) throws LineNotFoundException {
        return createLineResponse(lineRepository.findById(id).orElseThrow(LineNotFoundException::new));
    }

    public void updateLine(Long id, UpdateLineRequest updateLineRequest) throws LineNotFoundException {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        line.setName(updateLineRequest.getName());
        line.setColor(updateLineRequest.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private LineSectionResponse createLineSectionResponse(Line line) {
        List<SectionResponse> sectionResponses = line.getSections().get().stream()
                .map(this::createSectionResponse)
                .collect(Collectors.toList());
        return new LineSectionResponse(line.getId(), line.getName(), sectionResponses);
    }

    private SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(section.getId(), createStationResponse(section.getUpStation()), createStationResponse(section.getDownStation()), section.getDistance());
    }

    public LineSectionResponse showLineSections(Long id) throws LineNotFoundException {
        return createLineSectionResponse(lineRepository.findById(id).orElseThrow(LineNotFoundException::new));
    }

    public SectionResponse addLineSection(Long id, SectionRequest sectionRequest) throws CannotAddSectionException {
        Line line = lineRepository.findById(id).get();

        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).get();
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).get();

        Section section = new Section(line, upStation, downStation, sectionRequest.getDistance());
        line.addSection(section);
        return createSectionResponse(section);
    }

    public void deleteLineSection(Long id, Long stationId) throws CannotDeleteSectionException {
        Line line = lineRepository.findById(id).get();
        line.deleteSection(stationId);
    }
}
