package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.line.CreateLineRequest;
import nextstep.subway.applicaion.dto.line.LineResponse;
import nextstep.subway.applicaion.dto.line.UpdateLineRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineService(
            StationRepository stationRepository,
            LineRepository lineRepository,
            SectionRepository stationToLineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = stationToLineRepository;
    }

    @Transactional
    public LineResponse saveLine(CreateLineRequest createLineRequest) {
        final Station upStation = getStationByIdIfExists(createLineRequest.getUpStationId());
        final Station downStation = getStationByIdIfExists(createLineRequest.getDownStationId());
        final Line savedSubwayLine = lineRepository.save(new Line(
                createLineRequest.getName(),
                createLineRequest.getDistance(),
                createLineRequest.getColor(),
                upStation,
                downStation
        ));
        linkingStationAndSubwayLine(savedSubwayLine, upStation, downStation);

        return new LineResponse(savedSubwayLine);
    }

    public List<LineResponse> findAllLines() {
        final List<LineResponse> findSubwayLines = lineRepository.findAll()
                .stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());

        return findSubwayLines;
    }

    public LineResponse findOneLineById(Long lineId) {
        final Line findSubwayLine = getLineByIdIfExists(lineId);

        return new LineResponse(findSubwayLine);
    }

    @Transactional
    public LineResponse updateSubwayLine(Long lineId, UpdateLineRequest updateLineRequest) {
        final Line subwayLine = getLineByIdIfExists(lineId);
        subwayLine.update(updateLineRequest.getName(), updateLineRequest.getColor());

        return new LineResponse(subwayLine);
    }

    @Transactional
    public void performDeleteLine(Long lineId) {
        final Line line = getLineByIdIfExists(lineId);
        final Section section = getSectionIfExists(line, line.getUpStation(), line.getDownStation());

        line.performDelete(section);
        sectionRepository.delete(section);
    }

    @Transactional
    public void deleteSubwayLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    private Station getStationByIdIfExists(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 지하철 역입니다."));
    }

    private Line getLineByIdIfExists(Long subwayLineId) {
        return lineRepository.findById(subwayLineId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 지하철 노선입니다."));
    }

    private Section getSectionIfExists(Line line, Station upStation, Station downStation) {
        return sectionRepository.findByUpStationAndDownStationAndLine(upStation, downStation, line)
                .orElseThrow(() -> new NotFoundException("연결되어있지 않은 지하철역과 지하철 노선의 관계입니다."));
    }

    private void linkingStationAndSubwayLine(Line line, Station upStation, Station downStation) {
        final Section savedSection = sectionRepository.save(new Section(line, upStation, downStation));

        upStation.setLine(line);
        downStation.setLine(line);
        line.addSection(savedSection);
    }
}
