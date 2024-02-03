package subway.api.domain.model.entity;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author : Rene Choi
 * @since : 2024/02/03
 */
class SectionsTest {

	@Test
	@DisplayName("구간 추가 - 성공 케이스")
	void addSection_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station downStation = new Station(2L, "역삼역");
		Section section = new Section(1L, upStation, downStation, 10L);
		Sections sections = new Sections();

		// when
		sections.addSection(section);

		// then
		assertThat(sections.getSections()).contains(section);
	}

	@Test
	@DisplayName("마지막 구간 제거 - 성공 케이스")
	void removeLastSection_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station downStation = new Station(2L, "역삼역");
		Section section = new Section(1L, upStation, downStation, 10L);
		Sections sections = new Sections();
		sections.addSection(section);

		// when
		sections.removeLastSection();

		// then
		assertThat(sections.getSections()).doesNotContain(section);
	}

	@Test
	@DisplayName("중간 구간 추가 - 성공 케이스")
	void addMiddleSection_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station middleStation = new Station(3L, "선릉역");
		Station downStation = new Station(2L, "역삼역");
		Section firstSection = new Section(1L, upStation, downStation, 10L);
		Section middleSectionToAdd = new Section(2L, upStation, middleStation, 5L);
		Sections sections = new Sections();
		sections.addSection(firstSection);

		// when
		sections.addSection(middleSectionToAdd);

		// then
		assertThat(sections.getSections())
			.containsExactlyInAnyOrder(firstSection, middleSectionToAdd);
	}

	@Test
	@DisplayName("구간 내 역 검색 - 성공 케이스")
	void findStationInSection_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station downStation = new Station(2L, "역삼역");
		Section section = new Section(1L, upStation, downStation, 10L);
		Sections sections = new Sections();
		sections.addSection(section);

		// when
		boolean result = sections.isContainsAnyStation(2L);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("구간으로부터 역 목록 파싱 - 성공 케이스")
	void parseStationsFromSections_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station downStation = new Station(2L, "역삼역");
		Section section = new Section(1L, upStation, downStation, 10L);
		Sections sections = new Sections();
		sections.addSection(section);

		// when
		List<Station> result = sections.parseStations();

		// then
		assertThat(result).containsExactlyInAnyOrder(upStation, downStation);
	}


	@Test
	@DisplayName("구간 목록에서 특정 역을 포함하는 모든 구간 찾기 - 성공 케이스")
	void findSectionsIncludingStation_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station middleStation = new Station(3L, "선릉역");
		Station downStation = new Station(2L, "역삼역");
		Section firstSection = new Section(1L, upStation, middleStation, 5L);
		Section secondSection = new Section(2L, middleStation, downStation, 7L);
		Sections sections = new Sections();
		sections.addSection(firstSection);
		sections.addSection(secondSection);

		// when
		List<Section> result = sections.getSections().stream()
			.filter(section -> section.isAnyStation(middleStation.getId()))
			.collect(Collectors.toList());

		// then
		assertThat(result).containsExactlyInAnyOrder(firstSection, secondSection);
	}

	@Test
	@DisplayName("역 목록에서 특정 역을 제외한 목록 반환 - 성공 케이스")
	void excludeStationFromList_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station middleStation = new Station(3L, "선릉역");
		Station downStation = new Station(2L, "역삼역");
		Section firstSection = new Section(1L, upStation, middleStation, 5L);
		Section secondSection = new Section(2L, middleStation, downStation, 7L);
		Sections sections = new Sections();
		sections.addSection(firstSection);
		sections.addSection(secondSection);

		// when
		List<Station> result = sections.parseStations().stream()
			.filter(station -> !station.getId().equals(middleStation.getId()))
			.collect(Collectors.toList());

		// then
		assertThat(result).containsExactlyInAnyOrder(upStation, downStation);
	}


}
