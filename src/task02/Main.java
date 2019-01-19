package task02;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {

        String request = getRequest();

        HttpRequest httpRequest = new HttpRequestImpl(request);

        System.out.println(httpRequest.toString());
    }

    @NotNull
    private static String getRequest() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        StringBuilder request = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null && line.length() > 0) {
            request.append(line).append("\r\n");
        }

        if ((line = reader.readLine()) != null && line.length() > 0) {
            request.append(System.lineSeparator());
        }

        request.append(line);

        return request.toString();
    }

}
