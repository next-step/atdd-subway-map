package subway.service;

import org.springframework.expression.ExpressionException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
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
        Section section = sectionRepository.save(new Section(lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));

        List<Section> sections = new ArrayList<>();
        sections.add(section);

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), sections));
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

    public LineResponse addSection(String id, SectionRequest sectionRequest) throws Exception {
        Line line = lineRepository.findById(Long.valueOf(id)).orElseThrow(() -> new ExpressionException("[SYS_ERROR] do not found station by id ("+id+")"));

        if (line.isUpStationNotEqualDownStation(sectionRequest.getUpStationId())) {
            throw new Exception("[SYS_ERROR] upStation's not correct");
        }

        if (line.alreadyExistsDownStation(sectionRequest.getDownStationId())) {
            throw new Exception("[SYS_ERROR] downStation's already exists");
        }

        Section section = sectionRepository.save(new Section(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance()));
        line.addSection(section);

        line = lineRepository.save(line);

        return createLineResponse(line);

    }
}
