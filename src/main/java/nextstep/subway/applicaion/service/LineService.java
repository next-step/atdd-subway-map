package nextstep.subway.applicaion.service;

import nextstep.subway.applicaion.dto.request.SectionRequest;
import nextstep.subway.applicaion.dto.response.LineCreationResponse;
import nextstep.subway.applicaion.dto.request.LineRequest;
import nextstep.subway.applicaion.dto.response.LineResponse;
import nextstep.subway.applicaion.dto.response.SectionResponse;
import nextstep.subway.domain.Entity.Line;
import nextstep.subway.domain.Entity.Section;
import nextstep.subway.domain.Entity.Station;
import nextstep.subway.domain.Repository.LineRepository;
import nextstep.subway.domain.Repository.SectionRepository;
import nextstep.subway.domain.Repository.StationRepository;
import nextstep.subway.exception.DuplicationException;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final static int NUMBER_ZERO = 0;

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(SectionRepository sectionRepository,
                       LineRepository lineRepository,
                       StationRepository stationRepository
    ) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineCreationResponse saveLine(LineRequest request) {
        verifyDuplication(request.getName());
        Line line = lineRepository.save(
                new Line(request.getName(),
                        request.getColor(),
                        request.getUpStationId(),
                        request.getDownStationId(),
                        request.getDistance()));

        return new LineCreationResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    private void verifyDuplication(final String name) {
        Line line = lineRepository.findByName(name);

        if (!ObjectUtils.isEmpty(line)) {
            throw new DuplicationException("이미 존재합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(final Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);

        return createLineResponse(line);
    }

    public void updateLine(final Long id, final LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        line.updateLine(request.getColor(), request.getName());
        lineRepository.save(line);
    }

    public void deleteLine(final Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        lineRepository.delete(line);
    }

    private LineResponse createLineResponse(final Line line) {

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                Collections.EMPTY_LIST,
                line.getCreatedDate(),
                line.getModifiedDate());
    }

    public SectionResponse createSection(final Long id, final SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("노선이 존재하지 않습니다." + "LineId : " + id));
        Station upStation =
                stationRepository.findById(sectionRequest.getUpStationId())
                        .orElseThrow(() -> new NotFoundException("상행역이 존재하지 않습니다." + "LineId : " + id));
        Station downStation =
                stationRepository.findById(sectionRequest.getDownStationId())
                        .orElseThrow(() -> new NotFoundException("하행역이 존재하지 않습니다." + "LineId : " + id));
        verifyStationsRelation(line.getDownStationId(), upStation);
        newUpStationMustBeDownStation(upStation);
        Section section = sectionRepository.save(Section.of(line, upStation, downStation, sectionRequest.getDistance()));

        return SectionResponse.of(section.getId(), upStation.getId(), downStation.getId(), section.getDistance());
    }

    // 노선에서 이 등록하고자 하는 상행역이 하행역으로 등록되어있는지 확인해야한다.
    private void verifyStationsRelation(final Long downStationId, final Station upStation) {
        Station downStation = stationRepository.findById(downStationId)
                .orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다." + "stationId : " + downStationId));

        if (downStation.getId() != upStation.getId()) {

            throw new RuntimeException("등록하고자 하는 상행역이 하행역으로 등록되어있어야합니다.");
        }
    }

    private void newUpStationMustBeDownStation(final Station upStation) {
        if (NUMBER_ZERO != sectionRepository.count()) {
            Section sectionByDownStation = sectionRepository.findSectionByDownStation(upStation);
            if (ObjectUtils.isEmpty(sectionByDownStation)) {

                throw new RuntimeException("새로운 상행역은 무조건 하행역으로 등록되어있어야합니다.");
            }
        }
    }

    public void deleteSection(final Long lineId, final Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        Station downStation = stationRepository.findById(stationId).orElseThrow(RuntimeException::new);
        Section section = sectionRepository.findByLineAndDownStation(line, downStation).orElseThrow(RuntimeException::new);
        sectionRepository.delete(section);
    }


    private void deleteSectionMustBeDownStation(final Long stationId) {

    }
}
