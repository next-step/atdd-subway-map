package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.*;
import nextstep.subway.exception.DuplicatedLineException;
import nextstep.subway.exception.IllegalDeleteSectionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    public LineResponse saveLine(LineRequest lineRequest) {
        checkDuplication(lineRequest);
        Station upStation = stationRepository.findById(lineRequest.getUpStationId())
                .orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
                .orElseThrow(EntityNotFoundException::new);
        Section section = new Section(upStation, downStation, lineRequest.getDistance());
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), section);
        section.setLine(line);
        lineRepository.save(line);

        return LineResponse.of(line);
    }

    private void checkDuplication(LineRequest request) {
        if (lineRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicatedLineException();
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineResponse.of(line)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        return lineRepository.findById(id)
                .map(line -> LineResponse.of(line)).orElseThrow(() -> new EntityNotFoundException());
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        line.update(lineRequest.getName(), lineRequest.getColor());
        return LineResponse.of(line);
    }

    public void deleteLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        lineRepository.delete(line);
    }

    public LineResponse addSection(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(EntityNotFoundException::new);
        Section section = new Section(upStation, downStation, sectionRequest.getDistance());
        line.addSection(section);
        section.setLine(line);
        return LineResponse.of(line);
    }

    public void deleteSection(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        Line line = lineRepository.findById(section.getLine().getId())
                .orElseThrow(EntityNotFoundException::new);
        if (line.getSections().getSections().size() <= 1) {
            throw new IllegalDeleteSectionException();
        }
        line.getSections().getSections().remove(section);
    }
}