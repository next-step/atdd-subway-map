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

    public SectionResponse registerSection(long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow();
        Long upStationId = sectionRequest.getUpStationId();
        Long downEndpointStationId = line.getDownEndpoint().getId();
        if (!canRegisterSection(upStationId, downEndpointStationId)) {
            throw new IllegalArgumentException(
                ExceptionMessages.getNoEndpointInputExceptionMessage(upStationId, downEndpointStationId));
        }
        Section section = saveSection(line, sectionRequest.getDistance());
        changeDownEndpoint(sectionRequest, line);
        return SectionResponse.convertedByEntity(section);
    }

    private void changeDownEndpoint(SectionRequest sectionRequest, Line line) {
        Station downEndpoint = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow();
        line.modifyDownEndpoint(downEndpoint);
        lineRepository.save(line);
    }

    private boolean canRegisterSection(long upStationId, long downEndpoint) {
        return downEndpoint == upStationId;
    }

    private Station getStation(long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoStationExceptionMessage(stationId)));
    }

    private Section saveSection(Line line, long distance) {
        Station upStation = getStation(line.getUpEndpoint().getId());
        Station downStation = getStation(line.getDownEndpoint().getId());
        Section section = new Section(line, upStation, downStation, distance);
        return sectionRepository.save(section);
    }
}
