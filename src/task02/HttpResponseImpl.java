package task02;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseImpl implements HttpResponse {

    private HashMap<String, String> headers;
    private int statusCode;
    private byte[] content;

    HttpResponseImpl(HttpRequest httpRequest) {
        headers = new HashMap<>();
        addAllHeaders(httpRequest.getHeaders());
        parseStatusCode(httpRequest);
        parseContent(httpRequest);
    }

    private void parseContent(HttpRequest httpRequest) {

        switch (this.getStatusCode()) {

            case 200:
                byte[] decodedBytes = Base64.getDecoder().decode(headers.get("Authorization").split(" ")[1]);
                String userName = new String(decodedBytes);
                String firstRequestBodyParameter = "";
                String secondRequestBodyParameter = "";
                String thirdRequestBodyParameter = "";
                int counter = 1;

                for (Map.Entry<String, String> body : httpRequest.getBodyParameters().entrySet()) {

                    switch (counter) {
                        case 1:
                            firstRequestBodyParameter = body.getValue();
                            break;
                        case 2:
                            secondRequestBodyParameter = body.getKey() + " - " + body.getValue();
                            break;
                        case 3:
                            thirdRequestBodyParameter = body.getKey() + " - " + body.getValue();
                            break;
                    }
                    counter++;
                }

                String resultContent = (String.format("Greetings %s! You have successfully created %s with %s, %s."
                        , userName, firstRequestBodyParameter,
                        secondRequestBodyParameter,
                        thirdRequestBodyParameter));

                this.setContent(resultContent.getBytes());
                break;
            case 400:
                this.setContent("There was an error with the requested functionality due to malformed request.".getBytes());
                break;
            case 401:
                this.setContent("You are not authorized to access the requested functionality.".getBytes());
                break;
            case 404:
                this.setContent("The requested functionality was not found.".getBytes());
                break;
        }
    }


    private void parseStatusCode(HttpRequest httpRequest) {

        this.setStatusCode(200);

        if (httpRequest.getRequestUrl().isEmpty()) {
            this.setStatusCode(404);
        }

        if (!httpRequest.getHeaders().containsKey("Authorization")) {
            this.setStatusCode(401);
        }

        if (httpRequest.getMethod().equals("POST") && !httpRequest.isResource()) {
            this.setStatusCode(400);
        }


    }

    private void addAllHeaders(HashMap<String, String> headers) {
        for (Map.Entry<String, String> h : headers.entrySet()) {
            if (h.getKey().equals("Host") || h.getKey().equals("Date") ||
                    h.getKey().equals("Content-Type") ||
                    h.getKey().equals("Authorization")) {

                this.addHeader(h.getKey(), h.getValue());
            }
        }
    }

    @Override
    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public byte[] getContent() {
        return this.content;
    }

    @Override
    public byte[] getBytes() throws IOException {
        StringBuilder result = new StringBuilder();

        String requestStatus = "";

        switch (this.getStatusCode()) {
            case 200:
                requestStatus = "200 OK";
                break;
            case 400:
                requestStatus = "400 Bad Request";
                break;
            case 401:
                requestStatus = "401 Unauthorized";
                break;
            case 404:
                requestStatus = "404 Not Found";
                break;
        }

        result.append(String.format("HTTP/1.1 %s", requestStatus))
                .append(System.lineSeparator());

        for (Map.Entry<String, String> headerAndValue : this.getHeaders().entrySet()) {
            result.append(String.format(
                    "%s: %s", headerAndValue.getKey(), headerAndValue.getValue()))
                    .append(System.lineSeparator());
        }

        result.append(System.lineSeparator());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(result.toString().getBytes());
        outputStream.write(this.getContent());

        return outputStream.toByteArray();
    }

    @Override
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public void addHeader(String header, String value) {
        this.headers.put(header, value);
    }
}