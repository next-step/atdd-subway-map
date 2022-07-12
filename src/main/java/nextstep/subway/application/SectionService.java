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
        Line line = lineRepository.findById(lineId)
                                  .orElseThrow(() -> new CustomException(ResponseCode.LINE_NOT_FOUND));

        if(line.getDownStation().getId() != request.getUpStationId()){
            throw new CustomException(ResponseCode.LINE_NOT_FOUND);
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

        Station upStation = stationRepository.findById(request.getUpStationId())
                                             .orElseThrow(() -> new CustomException(ResponseCode.STATION_NOT_FOUND));
        Station downStation = stationRepository.findById(request.getUpStationId())
                                             .orElseThrow(() -> new CustomException(ResponseCode.STATION_NOT_FOUND));
        Section section = Section.builder().build();
        return SectionResponse.of(section);
    }
}
