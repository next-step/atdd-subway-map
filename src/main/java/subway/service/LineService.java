package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import java.util.List;
import java.util.stream.Collectors;

import static subway.service.SectionService.ERROR_NO_FOUND_SECTION;

@Service
@Transactional(readOnly = true)
public class LineService {
    private static String ERROR_NO_FOUND_LINE = "[SYS_ERROR] do not found line by id";
    private static String ERROR_UPSTATION_INVAILD = "[SYS_ERROR] upStation's not equal (last down station)";
    private static String ERROR_DOWNSTATION_INVAILD = "[SYS_ERROR] downStation's already exists";
    private static String ERROR_LINE_DELETE_SECTION_COUNT = "[SYS_ERROR] line have only one section";
    private static String ERROR_LINE_DELETE_SECTION_NO_LAST_SECTION = "[SYS_ERROR] section's not last section";

    LineRepository lineRepository;
    SectionRepository sectionRepository;
    StationRepository stationRepository;


    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) throws Exception{
        Section section = new Section(lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));

        line.addSection(section);
        sectionRepository.save(section);

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines(){
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        try {
            return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getSections());
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return new LineResponse();
        }
    }

    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id).map(this::createLineResponse).get();
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();

        line.change(lineRequest.getName(), lineRequest.getColor());

        lineRepository.save(line);
    }

    @Transactional
    public LineResponse addSection(String id, SectionRequest sectionRequest) throws Exception {
        Line line = lineRepository.findById(Long.valueOf(id)).orElseThrow(() -> new Exception(ERROR_NO_FOUND_LINE));

        if (! line.isUpStationEqualDownStation(sectionRequest.getUpStationId())) {
            throw new Exception(ERROR_UPSTATION_INVAILD);
        }

        if (line.alreadyExistsDownStation(sectionRequest.getDownStationId())) {
            throw new Exception(ERROR_DOWNSTATION_INVAILD);
        }

        Section section = sectionRepository.save(new Section(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance()));
        line.addSection(section);

        line = lineRepository.save(line);
        return createLineResponse(line);
    }

    @Transactional
    public LineResponse deleteSection(String id, String sectionId) throws Exception{
        Line line = lineRepository.findById(Long.valueOf(id)).orElseThrow(() -> new Exception(ERROR_NO_FOUND_LINE));
        Section section = sectionRepository.findById(Long.valueOf(sectionId)).orElseThrow(() -> new Exception(ERROR_NO_FOUND_SECTION));

        if (line.isNotValidSectionCount()) {
            throw new Exception(ERROR_LINE_DELETE_SECTION_COUNT);
        }

        if (line.isNotLastSection(section)) {
            throw new Exception(ERROR_LINE_DELETE_SECTION_NO_LAST_SECTION);
        }

        sectionRepository.deleteById(section.getId());
        line.deleteSection(section);

        Line deleteLine = lineRepository.save(line);
        return createLineResponse(deleteLine);
    }
}
