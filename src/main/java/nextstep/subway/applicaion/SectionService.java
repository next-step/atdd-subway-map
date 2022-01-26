package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final LineService lineService;
    private final StationService stationService;

    public SectionResponse saveSection(SectionRequest request, Long lineId) {
        Line line = lineService.findById(lineId);
        validateSaveSection(new Sections(line.getSections()), request);

        Station upStation = stationService.findById(request.getUpStationId(), "상행역이 존재하지 않습니다.");
        Station downStation = stationService.findById(request.getDownStationId(), "하행역이 존재하지 않습니다.");

        Section section = Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();

        return SectionResponse.of(sectionRepository.save(section));
    }

    private void validateSaveSection(Sections sections, SectionRequest request) {
        if(!sections.isDownStation(request.getUpStationId())) {
            throw new BadRequestException("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
        }

        if(sections.isRegisteredStation(request.getDownStationId())) {
            throw new BadRequestException("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없습니다.");
        }
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineService.findById(lineId);

        Sections sections = new Sections(line.getSections());
        validateDeleteSection(sections, stationId);

        line.getSections().remove(sections.getLastSection());
    }

    private void validateDeleteSection(Sections sections, Long stationId) {
        if(!sections.canDelete()) {
            throw new BadRequestException("지하철 노선의 구간이 1개인 경우 구간을 삭제할 수 없습니다.");
        }

        if(!sections.isDownStation(stationId)) {
            throw new BadRequestException("지하철 노선에 등록된 마지막 역만 제거할 수 있습니다.");
        }
    }
}
