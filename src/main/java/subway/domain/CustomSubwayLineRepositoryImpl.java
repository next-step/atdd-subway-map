package subway.domain;

import static subway.domain.QSection.*;
import static subway.domain.QSubwayLine.*;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import subway.presentation.response.SubwayLineResponse;

@RequiredArgsConstructor
public class CustomSubwayLineRepositoryImpl implements CustomSubwayLineRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<SubwayLineResponse.LineInfo> findSubwayLineProjectionAll() {
		return queryFactory
			.select(
				Projections.constructor(
					SubwayLineResponse.LineInfo.class,
					subwayLine
				)
			).from(subwayLine)
			.fetch();
	}

	@Override
	public Optional<SubwayLineResponse.LineInfo> findSubwayLineProjectionById(Long id) {
		SubwayLine findSubwayLine = findSubwayLineQuery().where(subwayLine.id.eq(id))
			.fetchOne();

		return findSubwayLine == null ? Optional.empty() : Optional.of(new SubwayLineResponse.LineInfo(findSubwayLine));
	}

	@Override
	public Optional<SubwayLine> findSubwayLineById(Long id) {
		SubwayLine findSubwayLine = findSubwayLineQuery()
			.innerJoin(subwayLine.sections.sections, section)
			.fetchJoin()
			.where(subwayLine.id.eq(id))
			.fetchOne();

		return findSubwayLine == null ? Optional.empty() : Optional.of(findSubwayLine);
	}

	private JPAQuery<SubwayLine> findSubwayLineQuery() {
		return queryFactory.selectFrom(subwayLine);
	}
}
