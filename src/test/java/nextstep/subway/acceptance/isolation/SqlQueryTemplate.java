package nextstep.subway.acceptance.isolation;

public enum SqlQueryTemplate {

	FOREIGN_KEY_CONSTRAINT_ENABLE("SET REFERENTIAL_INTEGRITY %s"),
	TRUNCATE_TABLE("TRUNCATE TABLE %s"),
	INITIALIZE_AUTO_INCREMENT("ALTER TABLE %s ALTER COLUMN ID RESTART WITH 1");

	private final String query;

	SqlQueryTemplate(String template) {
		this.query = template;
	}

	public String param(String param) {
		return String.format(this.query, param);
	}
}

