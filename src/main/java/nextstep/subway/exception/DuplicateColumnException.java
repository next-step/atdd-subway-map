package nextstep.subway.exception;

public class DuplicateColumnException extends RuntimeException {
    public DuplicateColumnException(ColumnName columnName) {
        super(ErrorMessage.DUPLICATE_COLUMN.getMessage(columnName.getName()));
    }
}
