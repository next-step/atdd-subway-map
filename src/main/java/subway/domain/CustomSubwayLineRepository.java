package subway.domain;

import java.util.List;
import java.util.Optional;

import subway.presentation.response.SubwayLineResponse;

public interface CustomSubwayLineRepository {

	List<SubwayLineResponse.LineInfo> findSubwayLineProjectionAll();

	Optional<SubwayLineResponse.LineInfo> findSubwayLineProjectionById(Long id);

	Optional<SubwayLine> findSubwayLineById(Long id);
}
