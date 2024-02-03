package subway.api.domain.model.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author : Rene Choi
 * @since : 2024/02/03
 */
class SectionTest {

	@Test
	@DisplayName("Section 객체 생성 - 성공 케이스")
	void sectionConstruction_success() {
		// Given
		Station upStation = mock(Station.class);
		Station downStation = mock(Station.class);
		Long distance = 10L;

		// When
		Section section = Section.of(upStation, downStation, distance);

		// Then
		assertNotNull(section);
		assertEquals(upStation, section.getUpStation());
		assertEquals(downStation, section.getDownStation());
		assertEquals(distance, section.getDistance());
	}

	@Test
	@DisplayName("하행 종점역 판별 - 성공 케이스")
	void isDownEndStation_success() {
		// Given
		Station upStation = mock(Station.class);
		Station downStation = mock(Station.class);
		Long distance = 10L;
		Section section = Section.of(upStation, downStation, distance);
		when(downStation.getId()).thenReturn(1L);

		// When
		boolean result = section.isDownEndStation(1L);

		// Then
		assertTrue(result);
	}

	@Test
	@DisplayName("상행 종점역 판별 - 성공 케이스")
	void isUpEndStation_success() {
		// Given
		Station upStation = mock(Station.class);
		Station downStation = mock(Station.class);
		Long distance = 10L;
		Section section = Section.of(upStation, downStation, distance);
		when(upStation.getId()).thenReturn(1L);

		// When
		boolean result = section.isUpEndStation(1L);

		// Then
		assertTrue(result);
	}

	@Test
	@DisplayName("임의의 역이 섹션에 포함되는지 판별 - 성공 케이스")
	void isAnyStation_success() {
		// Given
		Station upStation = mock(Station.class);
		Station downStation = mock(Station.class);
		Long distance = 10L;
		Section section = Section.of(upStation, downStation, distance);
		when(upStation.getId()).thenReturn(1L);
		when(downStation.getId()).thenReturn(2L);

		// When
		boolean resultForUpStation = section.isAnyStation(1L);
		boolean resultForDownStation = section.isAnyStation(2L);

		// Then
		assertTrue(resultForUpStation);
		assertTrue(resultForDownStation);
	}

	@Test
	@DisplayName("마지막 구간 삭제 - 성공 케이스")
	void removeLastSection_success() {
		// Given
		Station upStation = new Station(1L, "상행역");
		Station downStation = new Station(2L, "하행역");
		Long distance = 10L;
		Section section = new Section(1L, upStation, downStation, distance);
		Sections sections = new Sections(new TreeSet<>(Set.of(section)));

		// When
		sections.removeLastSection();

		// Then
		assertTrue(sections.getSections().isEmpty());
	}

	@Test
	@DisplayName("역 목록 파싱 - 성공 케이스")
	void parseStations_success() {
		// Given
		Station upStation1 = new Station(1L, "상행역1");
		Station downStation1 = new Station(2L, "하행역1");
		Station upStation2 = new Station(3L, "상행역2");
		Station downStation2 =  new Station(4L, "하행역2");
		Long distance = 10L;
		Section section1 = Section.of(upStation1, downStation1, distance);
		section1.setId(1L);
		Section section2 = Section.of(upStation2, downStation2, distance);
		section2.setId(2L);
		Sections sections = new Sections(new TreeSet<>(Set.of(section1, section2)));

		// When
		List<Station> result = sections.parseStations();

		// Then
		assertEquals(4, result.size());
	}


}