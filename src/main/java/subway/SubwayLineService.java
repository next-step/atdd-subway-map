package subway;


import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.exception.ResourceNotFoundException;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubwayLineService {

    private final SubwayLineRepository subwayLineRepository;
    private final StationRepository stationRepository;

    public SubwayLineResponse saveLine(SubwayLineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(
            () -> new ResourceNotFoundException("리소스를 찾을 수 없읍니다."));
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(
            () -> new ResourceNotFoundException("리소스를 찾을 수 없읍니다."));
        SubwayLine subwayLine = subwayLineRepository.save(
            new SubwayLine(request.getName(), request.getColor(), upStation, downStation,
                request.getDistance()));

        return SubwayLineResponse.createLineResponse(subwayLine);
    }
}
