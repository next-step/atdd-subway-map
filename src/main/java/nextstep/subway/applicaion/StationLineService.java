package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationLineService {
    private StationLineRepository stationLineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public StationLineService(StationLineRepository stationLineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationLineRepository = stationLineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public StationLineResponse saveStationLine(StationLineRequest stationLineRequest) {
        StationLine stationLine = stationLineRepository.save(stationLineRequest.toEntity());
        sectionRepository.save(stationLineRequest.toSection(stationLine));
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
        List<Station> stations = stationLine.getStationIdsIncluded()
                .stream()
                .map(id -> getStationOrThrow(id))
                .collect(Collectors.toList());

        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                stations
        );
    }

    private Station getStationOrThrow(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format("해당 %d의 id 값을 가진 Station은 존재하지 않습니다.", id), HttpStatus.BAD_REQUEST));
    }

    @Transactional
    public StationLineResponse registerSection(Long id, SectionRequest sectionRequest) {
        StationLine stationLine = getStationLineOrThrow(id);
        Section section = sectionRequest.toEntity(stationLine);
        Station upStation = getStationOrThrow(section.getUpStationId());
        Station downStation = getStationOrThrow(section.getDownStationId());

        List<Section> sectionsIncludedInLine = stationLine.getSections();
        Long lineDownStationId = sectionsIncludedInLine.get(sectionsIncludedInLine.size() - 1)
                .getDownStationId();
        List<Station> stationsIncludedInLine = stationLine.getStationIdsIncluded()
                .stream()
                .map(stationId -> getStationOrThrow(stationId))
                .collect(Collectors.toList());
        Station lineDownStation = getStationOrThrow(lineDownStationId);


        validateRegisterSectionCondition(upStation, downStation, stationsIncludedInLine, lineDownStation);

        sectionRepository.save(section);
        return createStationLineResponse(stationLine);
    }

    private void validateRegisterSectionCondition(Station upStation, Station downStation, List<Station> stationsIncludedInLine, Station lineDownStation) {
        if (!lineDownStation.equals(upStation)) {
            throw new BusinessException("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        if (stationsIncludedInLine.contains(downStation)) {
            throw new BusinessException("새로운 구간의 하행역이 해당 노선에 이미 등록되어있습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Station deleteStation = getStationOrThrow(stationId);
        StationLine stationLine = getStationLineOrThrow(id);
        List<Section> sections = stationLine.getSections();

        validateDeleteSectionCondition(deleteStation, sections);

        Section lineLastSection = sections.get(sections.size() - 1);
        sectionRepository.delete(lineLastSection);
    }

    private void validateDeleteSectionCondition(Station deleteStation, List<Section> sections) {
        Long lineDownStationId = sections.get(sections.size() - 1)
                .getDownStationId();
        Station targetStation = getStationOrThrow(lineDownStationId);
        if (!targetStation
                .equals(deleteStation)) {
            throw new BusinessException("삭제하려는 역이 하행 종점역이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        if (sections.size() == 1) {
            throw new BusinessException("지하철 노선에 상행 종점역과 하행 종점역만 존재합니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
