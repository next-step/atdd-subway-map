package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.LogicError;
import nextstep.subway.exception.LogicException;
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

    public LineResponse saveLine(LineRequest lineRequest) {

        if (isExistLineName(lineRequest.getName())) {
            throw new LogicException(LogicError.DUPLICATED_NAME_LINE);
        }

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        Station upStation = stationService.findById(lineRequest.getUpStationId());
        Station downStation = stationService.findById(lineRequest.getDownStationId());
        line.addSection(upStation, downStation, lineRequest.getDistance());

        return LineResponse.of(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = findById(id);
        return LineResponse.of(line);
    }

    public LineResponse modifyLine(Long id, LineRequest lineRequest) {
        Line line = findById(id);
        line.modify(lineRequest);
        Line modifiedLine = lineRepository.save(line);
        return LineResponse.of(modifiedLine);
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LogicException(LogicError.NOT_EXIST_LINE));
    }

    private boolean isExistLineName(String name) {
        return lineRepository.existsByName(name);
    }

    public SectionResponse saveSection(Long id, SectionRequest sectionRequest) {
        Line line = findById(id);
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        Section section = sectionRepository.save(new Section(line, upStation, downStation, sectionRequest.getDistance()));
        return SectionResponse.of(section);
    }
}
