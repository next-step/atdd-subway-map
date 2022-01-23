package nextstep.subway.acceptance.section;

public class SectionAcceptanceTestParameter {

	public String 상행역;
	public String 하행역;
	public int 구간_거리;

	public SectionAcceptanceTestParameter(String 상행역, String 하행역, int 구간_거리) {
		this.상행역 = 상행역;
		this.하행역 = 하행역;
		this.구간_거리 = 구간_거리;
	}
}
