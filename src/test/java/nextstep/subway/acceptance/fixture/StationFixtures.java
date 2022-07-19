package nextstep.subway.acceptance.fixture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum StationFixtures {
    강남역("강남역"),
    역삼역("역삼역"),
    양재역("양재역");


    public static final List<String> 이호선역_이름들 = List.of("강남역", "역삼역");
    public static final List<String> 신분당선역_이름들 = List.of("강남역", "양재역");

    private final String value;

}
