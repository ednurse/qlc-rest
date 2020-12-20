package biz.sigma7.qlcrest.domain.exception;

public class DownstreamException extends RuntimeException {

    public DownstreamException(String message, Throwable cause) {
        super (message, cause);
    }

}
