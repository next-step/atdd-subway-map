package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.exception.ExceptionMessages;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository,
        StationRepository stationRepository) {

        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public List<SectionResponse> getSections(long lineId) {
        List<Section> sections = sectionRepository.findSectionsByLineId(lineId);
        return sections.stream().map(SectionResponse::convertedByEntity).collect(Collectors.toList());
    }

    @Transactional
    public SectionResponse registerSection(long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoLineExceptionMessage(lineId)));
        Long upStationId = sectionRequest.getUpStationId();
        Long downEndpointStationId = line.getDownEndpoint().getId();
        checkRegisterEndpointId(upStationId, downEndpointStationId);
        Section section = saveSection(line, sectionRequest);
        registerDownEndpoint(sectionRequest, line);
        return SectionResponse.convertedByEntity(section);
    }

    @Transactional
    public void removeSection(long lineId, long stationId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoLineExceptionMessage(lineId)));
        checkRemoveEndPointId(stationId, line.getDownEndpoint().getId());
        checkSectionCount();
        removeDownEndpoint(line);
        sectionRepository.deleteSectionByDownStationIdAndLineId(stationId, lineId);
    }

    private void registerDownEndpoint(SectionRequest sectionRequest, Line line) {
        Station downEndpoint = stationRepository.findById(sectionRequest.getDownStationId())
            .orElseThrow(() -> new RuntimeException(
                ExceptionMessages.getNoStationExceptionMessage(sectionRequest.getDownStationId())));

        line.modifyDownEndpoint(downEndpoint);
        lineRepository.save(line);
    }

    private Station getStation(long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoStationExceptionMessage(stationId)));
    }

    private Section saveSection(Line line, SectionRequest sectionRequest) {
        Station upStation = getStation(sectionRequest.getUpStationId());
        Station downStation = getStation(sectionRequest.getDownStationId());
        Section section = new Section(line, upStation, downStation, sectionRequest.getDistance());
        return sectionRepository.save(section);
    }

    private void checkRegisterEndpointId(long upStationId, long downEndpointStationId) {
        if (upStationId != downEndpointStationId) {
            throw new IllegalArgumentException(
                ExceptionMessages.getNotEndpointInputExceptionMessage(upStationId, downEndpointStationId));
        }
    }

    private void checkRemoveEndPointId(long stationId, long downEndpointStationId) {
        if (downEndpointStationId != stationId) {
            throw new IllegalArgumentException(
                ExceptionMessages.getNotEndpointInputExceptionMessage(stationId, downEndpointStationId));
        }
    }

    private void checkSectionCount() {
        long sectionCount = sectionRepository.count();
        if (sectionCount == 1) {
            throw new RuntimeException(ExceptionMessages.getNeedAtLeastOneSectionExceptionMessage());
        }
    }

    private void removeDownEndpoint(Line line) {
        Section section = sectionRepository.findSectionByDownStationIdAndLineId(line.getDownEndpoint().getId(),
            line.getId());

        Station downStation = stationRepository.findById(section.getUpStation().getId())
            .orElseThrow(() -> new RuntimeException(
                ExceptionMessages.getNoStationExceptionMessage(section.getUpStation().getId())));

        line.modifyDownEndpoint(downStation);
        lineRepository.save(line);
    }
}
