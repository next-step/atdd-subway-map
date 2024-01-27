package subway.api.infrastructure.operators;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subway.api.domain.model.entity.Line;
import subway.api.domain.model.entity.Link;
import subway.api.domain.model.entity.Station;
import subway.api.domain.operators.LinkFactory;
import subway.api.infrastructure.persistence.LinkRepository;
import subway.api.infrastructure.persistence.StationRepository;
import subway.api.interfaces.dto.LineCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Component
@RequiredArgsConstructor
public class SimpleLinkFactory implements LinkFactory {
	private final StationRepository stationRepository;
	private final LinkRepository linkRepository;

	@Override
	public Link createLink(LineCreateRequest request, Line line) {

		Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
		Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();

		Link link = Link.of(upStation, downStation, request.getDistance(), line);

		return linkRepository.save(link);
	}
}
