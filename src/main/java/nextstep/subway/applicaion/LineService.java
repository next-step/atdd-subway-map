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

        Station upStation = stationService.findById(upStationId);
        Station downStation = stationService.findById(downStationId);
        Section section = new Section(upStation, downStation, distance);

        validateNameDuplicated(name);

        Line line = lineRepository.save(new Line(name, color, List.of(section)));
        section.setLine(line);
        sectionRepository.save(section);

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
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        Line line = findById(id);
        Section section = new Section(upStation, downStation, sectionRequest.getDistance());

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
}
