package task01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class httpParser {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        List<String> validUrls = getValidUrls();
        List<String> request = getRequest();

        String url = getUrl(request.get(0));
        String method = getMethod(request.get(0));

        Map<String, String> headers = getHeaders(request);
        Map<String, String> bodyParameters = getBodyParms(request);

        System.out.println();

        response(validUrls, headers, url, method, bodyParameters);
    }

    private static void response(List<String> validUrls, Map<String, String> headers, String url, String method, Map<String, String> bodyParameters) {
        if (!validUrls.contains(url)) {
            System.out.println("HTTP/1.1 404 Not Found");
            responseBody(headers);
            System.out.println("The requested functionality was not found.");
            return;
        }

        if (!headers.keySet().contains("Authorization")) {
            System.out.println("HTTP/1.1 401 Unauthorized");
            responseBody(headers);
            System.out.println("You are not authorized to access the requested functionality.");
            return;
        }

        if (method.equals("POST") && bodyParameters.size() == 0) {
            System.out.println("HTTP/1.1 400 Bad response");
            responseBody(headers);
            System.out.println("There was an error with the requested functionality due to malformed request.");
            return;
        }

        OkResponse(bodyParameters, headers);
    }

    private static void OkResponse(Map<String, String> bodyParameters, Map<String, String> headers) {

        byte[] decodedBytes = Base64.getDecoder().decode(headers.get("Authorization").split(" ")[1]);
        String userName = new String(decodedBytes);
        String firstRequestBodyParameter = "";
        String secondRequestBodyParameter = "";
        String thirdRequestBodyParameter = "";
        int counter = 0;


        for (Map.Entry<String, String> body : bodyParameters.entrySet()) {

            switch (counter) {
                case 0:
                    firstRequestBodyParameter = body.getValue();
                    break;
                case 1:
                    secondRequestBodyParameter = body.getKey() + " - " + body.getValue();
                    break;
                case 2:
                    thirdRequestBodyParameter = body.getKey() + " - " + body.getValue();
                    break;
            }
            counter++;
        }

        System.out.println("HTTP/1.1 200 OK");
        responseBody(headers);
        System.out.println(String.format("Greetings %s! You have successfully created %s with %s, %s."
                , userName, firstRequestBodyParameter,
                secondRequestBodyParameter,
                thirdRequestBodyParameter));
    }

    private static void responseBody(Map<String, String> headers) {
        if (headers.keySet().contains("Date")) System.out.println("Date: " + headers.get("Date"));
        if (headers.keySet().contains("Host")) System.out.println("Host: " + headers.get("Host"));
        if (headers.keySet().contains("Content-Type"))
            System.out.println("Content-Type: " + headers.get("Content-Type"));
        System.out.println();

    }

    private static List<String> getValidUrls() throws IOException {
        return Arrays.asList(reader.readLine().split("\\s+"));
    }

    private static List<String> getRequest() throws IOException {
        List<String> request = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null && line.length() > 0) {
            request.add(line);
        }

        if ((line = reader.readLine()) != null && line.length() > 0) {
            request.add(System.lineSeparator());
        }

        request.add(line);

        return request;
    }

    private static String getMethod(String line) {
        return line.split("\\s+")[0];
    }

    private static String getUrl(String line) {
        return line.split("\\s+")[1];
    }

    private static Map<String, String> getHeaders(List<String> request) {
        Map<String, String> headers = new LinkedHashMap<>();

        request.stream()
                .skip(1)
                .filter(h -> h.contains(": "))
                .map(h -> h.split(": "))
                .forEach(headerKvp ->
                        headers.put(headerKvp[0], headerKvp[1]));

        return headers;
    }

    private static Map<String, String> getBodyParms(List<String> request) {
        Map<String, String> bodyParameters = new LinkedHashMap<>();

        if (request.get(request.size() - 1).equals("")) {
            return bodyParameters;
        }

        Arrays.stream(request.get(request.size() - 1)
                .split("&"))
                .map(bp -> bp.split("="))
                .forEach(bpKvp ->
                        bodyParameters.put(bpKvp[0], bpKvp[1]));

        return bodyParameters;
    }
}
