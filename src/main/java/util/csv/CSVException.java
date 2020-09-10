package util.csv;


public class CSVException extends RuntimeException {
    CSVException(CSVErrorEnum error, Exception exception) {
        super(error.errorMessage, exception);
    }

    CSVException(CSVErrorEnum error) {
        super(error.errorMessage);
    }
}
