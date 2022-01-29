package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.DuplicateException;
import nextstep.subway.exception.NoElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;
    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationService stationService, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        String name = request.getName();
        String color = request.getColor();
        Long upStationId = request.getUpStationId();
        Long downStationId = request.getDownStationId();
        int distance = request.getDistance();

        validateNameDuplicated(name);

        Section section = convertToSection(upStationId, downStationId, distance);
        Line line = lineRepository.save(new Line(name, color, section));

        return LineResponse.of(line);
    }

    private void validateNameDuplicated(String name) {
        if (lineRepository.existsByName(name)) {
            throw new DuplicateException("Duplicate Line" + name);
        }
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        return LineResponse.of(findById(id));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findById(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public SectionResponse saveSection(Long id, SectionRequest sectionRequest) {
        Long upStationId = sectionRequest.getUpStationId();
        Long downStationId = sectionRequest.getDownStationId();
        int distance = sectionRequest.getDistance();

        Section section = convertToSection(upStationId, downStationId, distance);
        Line line = findById(id);

        line.addSection(section);
        Section savedSection = sectionRepository.save(section);

        return SectionResponse.of(savedSection.getId());
    }

    public void deleteSection(Long id, Long stationId){
        Station station = stationService.findById(stationId);
        Line line = findById(id);

        line.removeSection(station);
    }

    private Line findById(Long id){
        return lineRepository.findById(id).orElseThrow(NoElementException::new);
    }

    private Section convertToSection(Long upStationId, Long downStationId, int distance) {
        Station upStation = stationService.findById(upStationId);
        Station downStation = stationService.findById(downStationId);
        return new Section(upStation, downStation, distance);
    }

}
