package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.subwayLine.CreateSubwayLineRequest;
import nextstep.subway.applicaion.dto.subwayLine.SubwayLineResponse;
import nextstep.subway.applicaion.dto.subwayLine.UpdateSubwayLineRequest;
import nextstep.subway.domain.StationToSubwayLine;
import nextstep.subway.repository.StationToSubwayLineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.repository.SubwayLineRepository;
import nextstep.subway.domain.SubwayLineColor;
import nextstep.subway.repository.SubwayLineColorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private final StationRepository stationRepository;
    private final SubwayLineRepository subwayLineRepository;
    private final SubwayLineColorRepository subwayLineColorRepository;
    private final StationToSubwayLineRepository stationToSubwayLineRepository;

    public SubwayLineService(
            StationRepository stationRepository,
            SubwayLineRepository subwayLineRepository,
            SubwayLineColorRepository subwayLineColorRepository,
            StationToSubwayLineRepository stationToSubwayLineRepository) {
        this.stationRepository = stationRepository;
        this.subwayLineRepository = subwayLineRepository;
        this.subwayLineColorRepository = subwayLineColorRepository;
        this.stationToSubwayLineRepository = stationToSubwayLineRepository;
    }

    @Transactional
    public SubwayLineResponse saveSubwayLine(CreateSubwayLineRequest createSubwayLineRequest) {
        final Station upStation = getStationByIdIfExists(createSubwayLineRequest.getUpStationId());
        final Station downStation = getStationByIdIfExists(createSubwayLineRequest.getDownStationId());
        final SubwayLineColor subwayLineColor = getSubwayLineColorByCodeIfExists(createSubwayLineRequest.getColor());
        final SubwayLine savedSubwayLine = subwayLineRepository.save(new SubwayLine(
                createSubwayLineRequest.getName(),
                createSubwayLineRequest.getDistance(),
                subwayLineColor,
                upStation,
                downStation
        ));
        linkingStationAndSubwayLine(savedSubwayLine, List.of(upStation, downStation));

        return new SubwayLineResponse(savedSubwayLine);
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        final List<SubwayLineResponse> findSubwayLines = subwayLineRepository.findAll()
                .stream()
                .map(SubwayLineResponse::new)
                .collect(Collectors.toList());

        return findSubwayLines;
    }

    public SubwayLineResponse findOneSubwayLineById(Long subwayLineId) {
        final SubwayLine findSubwayLine = getSubwayLineByIdIfExists(subwayLineId);

        return new SubwayLineResponse(findSubwayLine);
    }

    @Transactional
    public SubwayLineResponse updateSubwayLine(Long subwayLineId, UpdateSubwayLineRequest updateSubwayLineRequest) {
        final SubwayLine subwayLine = getSubwayLineByIdIfExists(subwayLineId);
        final SubwayLineColor subwayLineColor = getSubwayLineColorByCodeIfExists(updateSubwayLineRequest.getColor());
        final SubwayLine updatedSubwayLine = subwayLine.update(updateSubwayLineRequest.getName(), subwayLineColor);

        return new SubwayLineResponse(updatedSubwayLine);
    }

    @Transactional
    public void performDeleteSubwayLine(Long subwayLineId) {
        final SubwayLine subwayLine = getSubwayLineByIdIfExists(subwayLineId);
        final StationToSubwayLine upStationToSubwayLine = getStationToSubwayLineIfExists(subwayLine, subwayLine.getUpStation());
        final StationToSubwayLine downStationToSubwayLine = getStationToSubwayLineIfExists(subwayLine, subwayLine.getDownStation());
        subwayLine.performDelete(upStationToSubwayLine, downStationToSubwayLine);
        stationToSubwayLineRepository.deleteAll(List.of(upStationToSubwayLine, downStationToSubwayLine));
    }

    @Transactional
    public void deleteSubwayLine(Long subwayLineId) {
        subwayLineRepository.deleteById(subwayLineId);
    }

    private Station getStationByIdIfExists(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 지하철 역입니다."));
    }

    private SubwayLine getSubwayLineByIdIfExists(Long subwayLineId) {
        return subwayLineRepository.findById(subwayLineId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 지하철 노선입니다."));
    }

    private SubwayLineColor getSubwayLineColorByCodeIfExists(String stationLineColorCode) {
        return subwayLineColorRepository.findByCode(stationLineColorCode)
                .orElseThrow(() -> new RuntimeException("사용할 수 없는 지하철 노선 색 코드입니다."));
    }

    private StationToSubwayLine getStationToSubwayLineIfExists(SubwayLine subwayLine, Station station) {
        return stationToSubwayLineRepository.findByStationAndSubwayLine(station, subwayLine)
                .orElseThrow(() -> new RuntimeException("연결되어있지 않은 지하철역과 지하철 노선의 관계입니다."));
    }

    private void linkingStationAndSubwayLine(SubwayLine subwayLine, List<Station> stations) {
        final List<StationToSubwayLine> stationToSubwayLines = new ArrayList<>();
        for (Station station : stations) {
            final StationToSubwayLine savedStationToSubwayLine = stationToSubwayLineRepository.save(new StationToSubwayLine(subwayLine, station));
            stationToSubwayLines.add(savedStationToSubwayLine);

            station.updateSubwayLine(savedStationToSubwayLine);
        }
        subwayLine.updateStations(stationToSubwayLines);
    }
}
