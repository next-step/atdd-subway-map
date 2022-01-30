package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.request.SectionRequest;
import nextstep.subway.applicaion.dto.response.SectionResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.NotFoundRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SectionResponse saveSection(Long lineId, SectionRequest request) throws NotFoundRequestException, BadRequestException {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundRequestException(String.format(LineService.LINE_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, lineId)));

        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new NotFoundRequestException(String.format(StationService.STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, request.getUpStationId())));

        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new NotFoundRequestException(String.format(StationService.STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, request.getDownStationId())));

        Section section = Section.createAddSection(line, upStation, downStation, request.getDistance());
        line.addSection(section);

        sectionRepository.save(section);
        return SectionResponse.createSectionResponse(section, lineId);
    }

    public void deleteStationById(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundRequestException(String.format(LineService.LINE_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, lineId)));
        Station downEndStation = line.getSections().get(line.getSections().size() - 1).getDownStation();
        Section section = sectionRepository.findByDownStationId(stationId);

        if (!downEndStation.equals(section.getDownStation())) {
            throw new BadRequestException("구간 삭제는 하행 종점역만 삭제할 수 있습니다.");
        }

        if (line.getSections().size() == 1) {
            throw new BadRequestException("구간 삭제는 구간이 2개 이상이어야 합니다.");
        }

        stationRepository.deleteById(stationId);
        sectionRepository.delete(section);
    }
}
