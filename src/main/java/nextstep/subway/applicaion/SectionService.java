package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import org.springframework.stereotype.Service;

@Service
public class SectionService {
    public SectionResponse saveSection(SectionRequest sectionRequest) {
        return new SectionResponse("1");
    }
}
