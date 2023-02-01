package subway.domain;

import static subway.domain.QStation.*;
import static subway.domain.QSubwayLine.*;
import static subway.domain.QSubwayLineStationGroup.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import subway.presentation.response.SubwayLineResponse;

@RequiredArgsConstructor
public class CustomSubwayLineRepositoryImpl implements CustomSubwayLineRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<SubwayLineResponse.LineInfo> findSubwayLineProjectionAll() {
		List<SubwayLine> subwayLines = findSubwayLineQuery().distinct()
			.fetch();

		return subwayLines.stream()
			.map(SubwayLineResponse.LineInfo::new)
			.collect(Collectors.toUnmodifiableList());
	}

	@Override
	public Optional<SubwayLineResponse.LineInfo> findSubwayLineProjectionById(Long id) {
		SubwayLine findSubwayLine = findSubwayLineQuery().where(subwayLine.id.eq(id))
			.fetchOne();

		return findSubwayLine == null ? Optional.empty() : Optional.of(new SubwayLineResponse.LineInfo(findSubwayLine));
	}

	@Override
	public Optional<SubwayLine> findSubwayLineById(Long id) {
		SubwayLine findSubwayLine = findSubwayLineQuery().where(subwayLine.id.eq(id))
			.fetchOne();

		return findSubwayLine == null ? Optional.empty() : Optional.of(findSubwayLine);
	}

	private JPAQuery<SubwayLine> findSubwayLineQuery() {
		return queryFactory.selectFrom(subwayLine)
			.innerJoin(subwayLine.subwayLineStationGroups, subwayLineStationGroup)
			.fetchJoin()
			.innerJoin(subwayLineStationGroup.station, station);
	}
}
