package task02;

import java.util.*;
import java.util.stream.Collectors;

public class HttpRequestImpl implements HttpRequest {

    private String requestUrl;
    private String method;
    private HashMap<String, String> headers;
    private HashMap<String, String> bodyParameters;

    HttpRequestImpl(String request) {

        this.headers = new HashMap<>();
        this.bodyParameters = new LinkedHashMap<>();
        parseRequest(request);
    }

    private void parseRequest(String request) {
        List<String> lines = Arrays.stream(request.split(System.lineSeparator())).collect(Collectors.toList());

        this.setRequestUrl(lines.get(0).split(" ")[1]);
        this.setMethod(lines.get(0).split(" ")[0]);

        for (int i = 1; i <= lines.size() - 3; i++) {

            String[] headerAndValue = lines.get(i).split(": ");

            this.addHeader(headerAndValue[0], headerAndValue[1]);
        }

        if (lines.get(lines.size() - 2).isEmpty()) {
            String[] body = lines.get(lines.size() - 1).split("&");

            for (String s : body) {

                String[] bodyParams = s.split("=");

                this.addBodyParameters(bodyParams[0], bodyParams[1]);
            }

        }else {

            String[] headerAndValue = lines.get(lines.size() - 1).split(": ");

            this.addHeader(headerAndValue[0], headerAndValue[1]);
        }

    }

    @Override
    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public HashMap<String, String> getBodyParameters() {
        return this.bodyParameters;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getRequestUrl() {
        return this.requestUrl;
    }

    @Override
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Override
    public void addHeader(String header, String value) {
        this.headers.put(header, value);
    }

    @Override
    public void addBodyParameters(String parameter, String value) {
        this.bodyParameters.put(parameter, value);
    }

    @Override
    public boolean isResource() {
        return !this.getBodyParameters().isEmpty();
    }
}
