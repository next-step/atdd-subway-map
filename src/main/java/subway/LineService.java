package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationService stationService;
    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationService stationService, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Section section = createSection(lineRequest.getUpStationId(), lineRequest.getDownStationId());
        Line line = Line.from(lineRequest, section);
        Line lineSaved = lineRepository.save(line);
        return LineResponse.from(lineSaved);

    }

    public LineResponse findById(Long id) {
        return LineResponse.from(getLine(id));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modify(Long id, LineModificationRequest lineModificationRequest) {
        Line line = getLine(id);
        line.modify(lineModificationRequest);
        lineRepository.save(line);
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.deleteById(id);
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("no line for id"));
    }

    @Transactional
    public LineResponse saveSection(Long id, SectionRequest request) {
        Line line = getLine(id);
        Section section = createSection(request.getUpStationId(), request.getDownStationId());
        sectionRepository.save(section);
        line.addSection(section);
        lineRepository.save(line);
        return LineResponse.from(line);
    }

    private Section createSection(Long upStationId, Long downStationId) {
        Station upStation = stationService.findById(upStationId);
        Station downStation = stationService.findById(downStationId);
        return new Section(upStation, downStation);
    }
}
