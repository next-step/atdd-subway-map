package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.common.exception.CustomException;
import nextstep.subway.common.exception.ResponseCode;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.ui.dto.section.CreateSectionRequest;
import nextstep.subway.ui.dto.section.SectionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionResponse createSection(long lineId, CreateSectionRequest request) {
        Line line = findLine(lineId);
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());

        if(!line.getDownStation().getId().equals(request.getUpStationId())){
            throw new CustomException(ResponseCode.SECTION_NOT_MATCH);
        }

        List<Section> sections = line.getStations();
        for(Section section : sections){
            long downstationId = request.getDownStationId();
            long sectionDownstaionId = section.getDownStation().getId();
            long sectionUpstationId = section.getUpStation().getId();
            if(downstationId == sectionDownstaionId || downstationId == sectionUpstationId){
                throw new CustomException(ResponseCode.LINE_STATION_DUPLICATE);
            }
        }

        Section section = Section.builder()
            .upStation(line.getUpStation())
            .downStation(downStation)
            .distance(request.getDistance())
            .line(line).build();
        sectionRepository.save(section);
        return SectionResponse.of(section);
    }

    private Station findStation(long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(() -> new CustomException(ResponseCode.STATION_NOT_FOUND));
    }

    private Line findLine(final Long lineId) {
        return lineRepository.findById(lineId)
                             .orElseThrow(() -> new CustomException(ResponseCode.LINE_NOT_FOUND));
    }
}
