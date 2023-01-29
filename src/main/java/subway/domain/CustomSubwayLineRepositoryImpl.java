package subway.domain;

import static subway.domain.QStation.*;
import static subway.domain.QSubwayLine.*;
import static subway.domain.QSubwayLineStationGroup.*;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import subway.presentation.response.SubwayLineResponse;

@RequiredArgsConstructor
public class CustomSubwayLineRepositoryImpl implements CustomSubwayLineRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<SubwayLineResponse.LineInfo> findSubwayLineAll() {
		List<SubwayLine> subwayLines = queryFactory.select(subwayLine)
			.from(subwayLine)
			.innerJoin(subwayLine.subwayLineStationGroups, subwayLineStationGroup)
			.fetchJoin()
			.innerJoin(subwayLineStationGroup.station, station)
			.distinct()
			.fetch();

		return subwayLines.stream()
			.map(SubwayLineResponse.LineInfo::new)
			.collect(Collectors.toUnmodifiableList());
	}
}
