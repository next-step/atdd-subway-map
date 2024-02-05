package subway.api.domain.service.impl;

import static org.springframework.http.HttpStatus.*;

import java.util.SortedSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.api.domain.dto.inport.SectionCreateCommand;
import subway.api.domain.dto.outport.SectionInfo;
import subway.api.domain.model.entity.Line;
import subway.api.domain.model.entity.Section;
import subway.api.domain.model.entity.Station;
import subway.api.domain.operators.LineResolver;
import subway.api.domain.operators.SectionFactory;
import subway.api.domain.operators.StationResolver;
import subway.api.domain.service.SectionService;
import subway.api.infrastructure.persistence.SectionRepository;
import subway.common.exception.LineNotFoundException;
import subway.common.exception.SectionDeletionNotValidException;
import subway.common.exception.SectionInsertionNotValidException;
import subway.common.exception.StationNotFoundException;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnhancedSectionService implements SectionService {
	private final LineResolver lineResolver;
	private final SectionFactory sectionFactory;
	private final StationResolver stationResolver;


	// 존재하는 섹션들 중에
	// 0) empty인 경우 -> 그냥 생성
	// 1) 상행과 상행이 같은 경우 -> 중간 삽입 케이스
	// 2) 하행과 하행이 같은 경우 -> 중간 삽입 케이스
	// 3) command의 하행과 존재하는 최상단 구간의 상행이 같은 경우 -> 최상단 추가 케이스
	// 4) command의 상행과 존재하는 최하단 구간의 하행이 같은 경우 -> 최하단 추가 케이스
	// 5) 상행 하행이 모두 같은 경우 -> 예외
	// 6) 아무 것도 같지 않은 경우 -> 예외
	// 이미 등록되어 있는 역은 노선에 등록될 수 없음 !!!
	@Override
	@Transactional
	public SectionInfo addSection(Long lineId, SectionCreateCommand createCommand) {
		Line line = lineResolver.fetchOptional(lineId).orElseThrow(() -> new LineNotFoundException(BAD_REQUEST));

		Station upStation = stationResolver.fetchOptional(createCommand.getUpStationId()).orElseThrow(() -> new StationNotFoundException(BAD_REQUEST));
		Station downStation = stationResolver.fetchOptional(createCommand.getDownStationId()).orElseThrow(() -> new StationNotFoundException(BAD_REQUEST));

		Section newSection = sectionFactory.createSection(createCommand, upStation, downStation);

		SortedSet<Section> existingSections = line.getSectionCollection().getSections();
		Section firstSection = existingSections.first();
		Section lastSection = existingSections.last();

		if (existingSections.isEmpty()) {
			// 0) empty인 경우 -> 그냥 생성
			line.addSection(newSection);
		} else {
			boolean isInserted = false;

			for (Section section : existingSections) {
				// 5) 상행 하행이 모두 같은 경우 -> 예외
				if (section.getUpStation().equals(upStation) && section.getDownStation().equals(downStation)) {
					throw new SectionInsertionNotValidException("이미 등록된 구간입니다.");
				}

				// 1) 상행과 상행이 같은 경우 -> 중간 삽입 케이스
				if (section.getUpStation().equals(upStation)) {
					adjustSectionsForInsertion(existingSections, newSection, section, true);
					isInserted = true;
					break;
				}

				// 2) 하행과 하행이 같은 경우 -> 중간 삽입 케이스
				if (section.getDownStation().equals(downStation)) {
					adjustSectionsForInsertion(existingSections, newSection, section, false);
					isInserted = true;
					break;
				}

				// 3) command의 하행과 존재하는 최상단 구간의 상행이 같은 경우 -> 최상단 추가 케이스
				if (newSection.getDownStation().equals(firstSection.getUpStation())) {
					adjustSectionsForBeginningInsertion(existingSections, newSection);
					isInserted = true;
				}

				// 4) command의 상행과 존재하는 최하단 구간의 하행이 같은 경우 -> 최하단 추가 케이스
				else if (newSection.getUpStation().equals(lastSection.getDownStation())) {
					adjustSectionsForEndInsertion(existingSections, newSection);
					isInserted = true;
				}

			}

			// 6) 아무 것도 같지 않은 경우 -> 예외
			if (!isInserted) {
				throw new SectionInsertionNotValidException("연결할 수 있는 구간이 없습니다.");
			}
		}

		return SectionInfo.from(newSection);
	}

	private void adjustSectionsForBeginningInsertion(SortedSet<Section> existingSections, Section newSection) {
		existingSections.add(newSection);
		sectionFactory.save(newSection);
	}

	private void adjustSectionsForEndInsertion(SortedSet<Section> existingSections, Section newSection) {
		existingSections.add(newSection);
		sectionFactory.save(newSection);
	}

	private void adjustSectionsForInsertion(SortedSet<Section> existingSections, Section newSection, Section matchedSection, boolean isUpMatch) {
		if (isUpMatch) {
			Section adjustedSection = new Section(null, newSection.getDownStation(), matchedSection.getDownStation(),
				matchedSection.getDistance() - newSection.getDistance());
			existingSections.remove(matchedSection);
			sectionFactory.delete(matchedSection);
			existingSections.add(newSection);
			existingSections.add(adjustedSection);
			sectionFactory.save(adjustedSection);

		} else {
			Section adjustedSection = new Section(null, matchedSection.getUpStation(), newSection.getUpStation(),
				matchedSection.getDistance() - newSection.getDistance());
			existingSections.remove(matchedSection);
			sectionFactory.delete(matchedSection);
			existingSections.add(newSection);
			existingSections.add(adjustedSection);
			sectionFactory.save(adjustedSection);
		}
	}


	@Override
	@Transactional
	public void deleteSection(Long lineId, Long stationId) {
		Line line = lineResolver.fetchOptional(lineId).orElseThrow(() -> new LineNotFoundException(BAD_REQUEST));

		if (line.isNotDownEndStation(stationId) || line.isSectionCountBelowThreshold(1)) {
			throw new SectionDeletionNotValidException();
		}

		line.removeLastSection();
	}
}
