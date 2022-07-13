package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationLineService {
    private StationLineRepository stationLineRepository;
    private StationRepository stationRepository;

    public StationLineService(StationLineRepository stationLineRepository, StationRepository stationRepository) {
        this.stationLineRepository = stationLineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationLineResponse saveStationLine(StationLineRequest stationLineRequest) {
        StationLine stationLine = stationLineRepository.save(stationLineRequest.toEntity());
        return createStationLineResponse(stationLine);
    }

    public StationLineResponse findStationLine(Long id) {
        StationLine stationLine = getStationLineOrThrow(id);
        return createStationLineResponse(stationLine);
    }

    private StationLine getStationLineOrThrow(Long id) {
        return stationLineRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format("해당 %d의 id 값을 가진 StationLine은 존재하지 않습니다.", id), HttpStatus.BAD_REQUEST));
    }

    public List<StationLineResponse> findAllStationLines() {
        return stationLineRepository.findAll()
                .stream()
                .map(this::createStationLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationLineById(Long id) {
        stationLineRepository.deleteById(id);
    }

    @Transactional
    public void updateStationLine(Long id, StationLineRequest stationLineRequest) {
        StationLine existedStationLine = getStationLineOrThrow(id);
        existedStationLine.updateByStationLineRequest(stationLineRequest);
    }

    private StationLineResponse createStationLineResponse(StationLine stationLine) {
        List<Station> stations = findRelatedStations(stationLine);

        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                stations
        );
    }

    private List<Station> findRelatedStations(StationLine stationLine) {
        List<Station> stations = new ArrayList<>();
        Station upStation = getStationOrThrow(stationLine.getUpStationId());
        Station downStation = getStationOrThrow(stationLine.getDownStationId());
        stations.add(upStation);
        stations.add(downStation);
        return stations;
    }

    private Station getStationOrThrow(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format("해당 %d의 id 값을 가진 Station은 존재하지 않습니다.", id), HttpStatus.BAD_REQUEST));
    }

    @Transactional
    public StationLineResponse registerSection(Long id, SectionRequest sectionRequest) {
        Station Upstation = getStationOrThrow(sectionRequest.getUpStationId());
        Station downStation = getStationOrThrow(sectionRequest.getDownStationId());

        StationLine stationLine = getStationLineOrThrow(id);
        Station lineUpstation = getStationOrThrow(stationLine.getUpStationId());
        Station lineDownStation = getStationOrThrow(stationLine.getDownStationId());

        List<Long> stationIdsIncludedInLine = stationLine.stationIdsIncludedInLine();
        List<Station> stationsIncludedInLine = stationIdsIncludedInLine.stream()
                .map(stationId -> getStationOrThrow(stationId))
                .collect(Collectors.toList());

        validateRegisterSectionCondition(lineUpstation, lineDownStation, stationsIncludedInLine, Upstation, downStation);

        stationLine.setDownStationId(downStation.getId());

        stationsIncludedInLine.add(Upstation);
        String newstationsIncludedInLine = stationListToIdStringSeperatedComma(stationsIncludedInLine);
        stationLine.setStationsIncluded(newstationsIncludedInLine);

        return createStationLineResponse(stationLine);
    }

    private String stationListToIdStringSeperatedComma(List<Station> stationsIncludedInLine) {
        return StringUtils.join(stationsIncludedInLine.stream()
                .map(station -> String.valueOf(station.getId()))
                .collect(Collectors.toList()), ",");
    }

    private void validateRegisterSectionCondition(Station lineUpstation, Station lineDownStation, List<Station> stationsIncludedInLine, Station Upstation, Station downStation) {
        if (!lineDownStation.equals(Upstation)) {
            throw new BusinessException("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        if (stationsIncludedInLine.contains(downStation) || lineUpstation.equals(downStation) || lineDownStation.equals(downStation)) {
            throw new BusinessException("새로운 구간의 하행역이 해당 노선에 이미 등록되어있습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Station deleteStation = getStationOrThrow(stationId);

        StationLine stationLine = getStationLineOrThrow(id);
        Station lineDownStation = getStationOrThrow(stationLine.getDownStationId());
        List<Long> stationIdsIncludedInLine = stationLine.stationIdsIncludedInLine();

        validateDeleteSectionCondition(lineDownStation, deleteStation, stationIdsIncludedInLine);

        Long newDownStationId = stationIdsIncludedInLine.get(stationIdsIncludedInLine.size() - 1);
        stationLine.setDownStationId(newDownStationId);

        String newstationsIncludedInLine = StringUtils.join(stationIdsIncludedInLine.remove(newDownStationId), ",");
        stationLine.setStationsIncluded(newstationsIncludedInLine);
    }

    private void validateDeleteSectionCondition(Station downStation, Station deleteStation, List<Long> stationIdsIncludedInLine) {
        if (!downStation.equals(deleteStation)) {
            throw new BusinessException("삭제하려는 역이 하행 종점역이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        if (stationIdsIncludedInLine.isEmpty()) {
            throw new BusinessException("지하철 노선에 상행 종점역과 하행 종점역만 존재합니다.", HttpStatus.BAD_REQUEST);
        }
        return;
    }
}
