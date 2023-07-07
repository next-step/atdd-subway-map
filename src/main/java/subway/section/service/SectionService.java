package subway.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.ErrorCode;
import subway.exception.SubwayException;
import subway.section.dto.SectionDto;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;
import subway.subwayline.entity.SubwayLine;
import subway.subwayline.repository.SubwayLineRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionService {

    private final StationRepository stationRepository;
    private final SubwayLineRepository subwayLineRepository;

    public void addSection(Long lineId, SectionDto sectionDto) {
        SubwayLine subwayLine = subwayLineRepository.findById(lineId)
                .orElseThrow(() -> new SubwayException(ErrorCode.BAD_REQUEST));

        // 신규 구간의 상행역
        Station upStation = stationRepository.findById(sectionDto.getUpStationId())
                .orElseThrow(() -> new SubwayException(ErrorCode.BAD_REQUEST));
        // 신규 구간의 하행역
        Station downStation = stationRepository.findById(sectionDto.getDownStationId())
                .orElseThrow(() -> new SubwayException(ErrorCode.BAD_REQUEST));

        validationStation(subwayLine, upStation, downStation);

        // 신귶 구간 등록
        subwayLine.addSection(sectionDto.toEntity(subwayLine, upStation, downStation, sectionDto.getDistance()));
    }

    public void removeSection(Long lineId, Long stationId) {
        SubwayLine subwayLine = subwayLineRepository.findById(lineId)
                .orElseThrow(() -> new SubwayException(ErrorCode.BAD_REQUEST));

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new SubwayException(ErrorCode.BAD_REQUEST));

        subwayLine.removeSection(station);
    }

    private void validationStation(SubwayLine subwayLine, Station upStation, Station downStation) {
        // 신규 구간의 상행역이 기존 구간의 하행역과 같은지 확인 아닐시 예외 처리
        subwayLine.isExistsDownStation(upStation);

        // 신규 구간의 하행역이 기존 구간의 역이 있는지 검사 있으면 예외 처리
        subwayLine.isExistsStations(downStation);
    }
}
