package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.line.CreateLineRequest;
import nextstep.subway.applicaion.dto.line.LineResponse;
import nextstep.subway.applicaion.dto.line.UpdateLineRequest;
import nextstep.subway.domain.Section;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.repository.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public LineResponse saveSubwayLine(CreateLineRequest createLineRequest) {
        final Station upStation = getStationByIdIfExists(createLineRequest.getUpStationId());
        final Station downStation = getStationByIdIfExists(createLineRequest.getDownStationId());
        final Line savedSubwayLine = lineRepository.save(new Line(
                createLineRequest.getName(),
                createLineRequest.getDistance(),
                createLineRequest.getColor(),
                upStation,
                downStation
        ));
        linkingStationAndSubwayLine(savedSubwayLine, List.of(upStation, downStation));

        return new LineResponse(savedSubwayLine);
    }

    public List<LineResponse> findAllSubwayLines() {
        final List<LineResponse> findSubwayLines = lineRepository.findAll()
                .stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());

        return findSubwayLines;
    }

    public LineResponse findOneSubwayLineById(Long subwayLineId) {
        final Line findSubwayLine = getSubwayLineByIdIfExists(subwayLineId);

        return new LineResponse(findSubwayLine);
    }

    @Transactional
    public LineResponse updateSubwayLine(Long subwayLineId, UpdateLineRequest updateLineRequest) {
        final Line subwayLine = getSubwayLineByIdIfExists(subwayLineId);
        final Line updatedSubwayLine = subwayLine.update(updateLineRequest.getName(), updateLineRequest.getColor());

        return new LineResponse(updatedSubwayLine);
    }

    @Transactional
    public void performDeleteSubwayLine(Long subwayLineId) {
        final Line subwayLine = getSubwayLineByIdIfExists(subwayLineId);
        final Section upStationToLine = getStationToSubwayLineIfExists(subwayLine, subwayLine.getUpStation());
        final Section downStationToLine = getStationToSubwayLineIfExists(subwayLine, subwayLine.getDownStation());
        subwayLine.performDelete(upStationToLine, downStationToLine);
        sectionRepository.deleteAll(List.of(upStationToLine, downStationToLine));
    }

    @Transactional
    public void deleteSubwayLine(Long subwayLineId) {
        lineRepository.deleteById(subwayLineId);
    }

    private Station getStationByIdIfExists(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 지하철 역입니다."));
    }

    private Line getSubwayLineByIdIfExists(Long subwayLineId) {
        return lineRepository.findById(subwayLineId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 지하철 노선입니다."));
    }

    private Section getStationToSubwayLineIfExists(Line subwayLine, Station station) {
        return sectionRepository.findByStationAndLine(station, subwayLine)
                .orElseThrow(() -> new RuntimeException("연결되어있지 않은 지하철역과 지하철 노선의 관계입니다."));
    }

    private void linkingStationAndSubwayLine(Line subwayLine, List<Station> stations) {
        final List<Section> sections = new ArrayList<>();
        for (Station station : stations) {
            final Section savedSection = sectionRepository.save(new Section(subwayLine, station));
            sections.add(savedSection);

            station.updateSubwayLine(savedSection);
        }
        subwayLine.updateStations(sections);
    }
}
