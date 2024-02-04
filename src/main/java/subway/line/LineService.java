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
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

//    private LineResponse createLineResponse(Line line) {
//        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(line.getUpStation(), line.getDownStation()));
//    }

    private LineResponse createLineResponse(Line line) {
        List<Section> sections = sectionRepository.findAllByLineId(line.getId());
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(sections.get(sections.size() - 1).getDownStation());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    public LineResponse createLine(LineRequest lineRequest) throws StationNotFoundException, CannotAddSectionException {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(StationNotFoundException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(StationNotFoundException::new);
//        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
        Line line = new Line(lineRequest.getName(), lineRequest.getColor());
        Section section = new Section(line, upStation, downStation, lineRequest.getDistance());
        lineRepository.save(line);
        sectionRepository.save(section);
//        lineRepository.save(line);
//        sectionRepository.saveAll(Section.firstSectionsOf(line));
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

    public LineSectionResponse showLineSections(Long id) throws LineNotFoundException {
        return createLineSectionResponse(lineRepository.findById(id).orElseThrow(LineNotFoundException::new));
    }

    private SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(section.getId(), createStationResponse(section.getUpStation()), createStationResponse(section.getDownStation()), section.getDistance());
//        return new SectionResponse(section.getStation().getId(), section.getStation().getName(), section.getDistanceFromPrev());
    }

    private LineSectionResponse createLineSectionResponse(Line line) {
        List<SectionResponse> sectionResponses = line.getSections().stream()
                .map(this::createSectionResponse)
                .collect(Collectors.toList());
        return new LineSectionResponse(line.getId(), line.getName(), sectionResponses);
//        return new LineSectionResponse(line.getId(), line.getName(), line.getUpStation().getId(), line.getDownStation().getId(), sectionResponses);
    }

    public LineSectionResponse addLineSection(Long id, SectionRequest sectionRequest) throws CannotAddSectionException {
        Line line = lineRepository.findById(id).get();
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).get();

//        Long increasedDistance = line.addSection(sectionRequest, downStation);

//        Section section = new Section(new SectionId(line.getId(), sectionRequest.getDownStationId()), increasedDistance);
//        section.setLine(line);
//        section.setStation(downStation);

//        sectionRepository.save(section);
        return createLineSectionResponse(line);
    }

    public void deleteLineSection(Long id, Long stationId) throws CannotDeleteSectionException {
        Line line = lineRepository.findById(id).get();
        Section section = sectionRepository.findById(new SectionId(line.getId(), stationId)).orElseThrow();
//        line.deleteSection(section);
        sectionRepository.delete(section);
    }
}
