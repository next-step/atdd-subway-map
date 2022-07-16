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
        Section newSection = createSection(stationLineRequest.getUpStationId(), stationLineRequest.getDownStationId(), stationLineRequest.getDistance());
        stationLine.addSection(newSection);
        return createStationLineResponse(stationLine);
    }

    public List<StationLineResponse> findAllStationLines() {
        return stationLineRepository.findAll()
                .stream()
                .map(this::createStationLineResponse)
                .collect(Collectors.toList());
    }

    public StationLineResponse findStationLine(Long id) {
        StationLine stationLine = getStationLineOrThrow(id);
        return createStationLineResponse(stationLine);
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

    private StationLine getStationLineOrThrow(Long id) {
        return stationLineRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format("해당 %d의 id 값을 가진 StationLine은 존재하지 않습니다.", id), HttpStatus.BAD_REQUEST));
    }

    private Station getStationOrThrow(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format("해당 %d의 id 값을 가진 Station은 존재하지 않습니다.", id), HttpStatus.BAD_REQUEST));
    }

    private StationLineResponse createStationLineResponse(StationLine stationLine) {
        List<Station> stations = stationLine.getStationsIncluded();

        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                stations
        );
    }

    @Transactional
    public StationLineResponse registerSection(Long id, SectionRequest sectionRequest) {
        StationLine stationLine = getStationLineOrThrow(id);
        Section newSection = createSection(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        stationLine.addSection(newSection);
        return createStationLineResponse(stationLine);
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Station deleteStation = getStationOrThrow(stationId);
        StationLine stationLine = getStationLineOrThrow(id);
        Sections sections = stationLine.getSections();

        sections.validateDeleteSectionCondition(deleteStation);

        Section lineLastSection = sections.getLastSection();
        stationLine.deleteSection(lineLastSection);
        sectionRepository.delete(lineLastSection);
    }

    private Section createSection(Long upstationId, Long downStationId, Long distance) {
        Station upStation = getStationOrThrow(upstationId);
        Station downStation = getStationOrThrow(downStationId);
        Section section = new Section(distance, upStation, downStation);
        Section newSection = sectionRepository.save(section);
        return newSection;
    }
}
