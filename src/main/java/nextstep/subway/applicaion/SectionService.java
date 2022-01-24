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

        validateSection(request, new Sections(line.getSections()));

        // 이렇게 elseThrow를 던지니 코드가 길어지는 느낌입니다
        // 그래도 이렇게 꼭 elseThrow를 해야하는지 의견을 구합니다.
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

    private void validateSection(SectionRequest request, Sections sections) {
        if(request.getUpStationId() != sections.getDownStationId()) {
            throw new BadRequestException("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
        }

        if(sections.isRegisteredStation(request.getDownStationId())) {
            throw new BadRequestException("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없습니다.");
        }
    }
}
