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

import static subway.common.errorMsgEnum.*;

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
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
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
        Line line = lineRepository.findById(Long.valueOf(id)).orElseThrow(() -> new Exception("["+ERROR_NO_FOUND_LINE.getCode()+"]"+ERROR_NO_FOUND_LINE.getMsg()));

        Section section = new Section(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        line.addSection(section);

        line = lineRepository.save(line);
        sectionRepository.save(section);

        return createLineResponse(line);
    }

    @Transactional
    public LineResponse deleteSection(String id, String sectionId) throws Exception{
        Line line = lineRepository.findById(Long.valueOf(id)).orElseThrow(() -> new Exception("["+ERROR_NO_FOUND_LINE.getCode()+"]"+ERROR_NO_FOUND_LINE.getMsg()));
        Section section = sectionRepository.findById(Long.valueOf(sectionId)).orElseThrow(() -> new Exception("["+ERROR_NO_FOUND_SECTION.getCode()+"]"+ERROR_NO_FOUND_SECTION.getMsg()));

        if (line.isNotValidSectionCount()) {
            throw new Exception("["+ERROR_DELETE_SECTION_COUNT_LINE.getCode()+"]"+ERROR_DELETE_SECTION_COUNT_LINE.getMsg());
        }

        if (line.isNotLastSection(section)) {
            throw new Exception("["+ERROR_DELETE_SECTION_NO_LAST_SECTION_LINE.getCode()+"]"+ERROR_DELETE_SECTION_NO_LAST_SECTION_LINE.getMsg());
        }

        sectionRepository.deleteById(section.getId());
        line.deleteSection(section);

        Line deleteLine = lineRepository.save(line);
        return createLineResponse(deleteLine);
    }
}
