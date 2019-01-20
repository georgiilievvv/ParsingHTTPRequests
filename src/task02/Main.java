package task02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws IOException {

        String request = getRequest();

        HttpRequest httpRequest = new HttpRequestImpl(request);

        HttpResponse httpResponse = new HttpResponseImpl(httpRequest);
        byte[] array = httpResponse.getBytes();
        System.out.println(new String(array, StandardCharsets.UTF_8));
    }

    private static String getRequest() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        StringBuilder request = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null && line.length() > 0) {
            request.append(line).append(System.lineSeparator());
        }

        if ((line = reader.readLine()) != null && line.length() > 0) {
            request.append(System.lineSeparator());
        }

        request.append(line);

        return request.toString();
    }

}
