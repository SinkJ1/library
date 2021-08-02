package sinkj1.library.web.rest.errors;

public class URISyntaxException extends java.net.URISyntaxException {

    public URISyntaxException(String input, String reason, int index) {
        super(input, reason, index);
    }

    public URISyntaxException(String input, String reason) {
        super(input, reason);
    }
}
