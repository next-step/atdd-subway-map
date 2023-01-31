package subway.section;

import org.springframework.stereotype.Service;
import subway.line.Line;
import subway.station.Station;

import java.util.List;

@Service
public class SectionService {

    Boolean isAppendable(final Line line, final SectionRequest sectionRequest) {
        Long downStationId = line.getDownStation().getId();
        Long requestUpStationId = sectionRequest.getUpStationId();

        if(!downStationId.equals(requestUpStationId))
            return Boolean.FALSE;

//        if(isAppended(stations, sectionRequest.getDownStationId()))
//            return Boolean.FALSE;

        return Boolean.TRUE;
    }

//
//    private Boolean isAppended(final List<Station> stations, final Long newStationId) {
//        return stations.stream()
//                .anyMatch((s) -> s.getId().equals(newStationId));
//    }
}
