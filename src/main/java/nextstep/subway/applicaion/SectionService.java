package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionResponse saveSection(SectionRequest request, Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 노선입니다."));

        List<Section> sections = line.getSections();
        List<Long> stationIds = sections.stream()
                .map(section1 -> section1.getUpStation().getId()).collect(Collectors.toList());
        stationIds.add(sections.get(sections.size() - 1).getDownStation().getId());

        validateSection(request, stationIds);

        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new RuntimeException("상행역이 존재하지 않습니다."));
        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new RuntimeException("하행역이 존재하지 않습니다."));

        Section section = Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();

        return SectionResponse.of(sectionRepository.save(section));
    }

    private void validateSection(SectionRequest request, List<Long> stationIds) {
        Long downStationId = stationIds.get(stationIds.size() - 1);
        if(request.getUpStationId() != downStationId) {
            throw new BadRequestException("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
        }

        if(stationIds.contains(request.getDownStationId())) {
            throw new BadRequestException("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없습니다.");
        }
    }
}
