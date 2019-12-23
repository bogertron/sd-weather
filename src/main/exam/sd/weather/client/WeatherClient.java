package exam.sd.weather.client;

import exam.sd.weather.bean.Location;
import exam.sd.weather.bean.Weather;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Client class for end to end testing of weather service
 */
public class WeatherClient {

    private String host;
    private BufferedReader scanner;
    private RestTemplate restTemplate;

    public WeatherClient() {
        this("localhost:8080");
    }

    public WeatherClient(String host) {
        this.host = host;
        this.scanner = new BufferedReader(new InputStreamReader(System.in));
        this.restTemplate = new RestTemplate();
    }

    public void printUsage() {
        System.out.println("1. Delete all");
        System.out.println("2. Delete range");
        System.out.println("3. Create new");
        System.out.println("4. List all");
        System.out.println("5. Usage");
        System.out.println("6. Quit");
    }

    public void run()  throws IOException {
        int input = -1;
        boolean cont = true;
        while (cont) {
            printUsage();
            input = readInt("");
            switch (input) {
                case 1:
                    delete();
                    break;
                case 2:
                    deleteRange();
                    break;
                case 3:
                    createNew();
                    break;
                case 4:
                    listAll();
                    break;
                case 5:
                    printUsage();
                    break;
                default:
                    cont = false;
                    break;
            }

        }
    }

    private Date readDate(String message)  throws IOException {
        Date result = null;
        String dateFormat = "yyyy-MM-dd";
        String input = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        while(result == null) {
            System.out.println(message + "(" + dateFormat + ")");
            try {
                input = scanner.readLine();
                result = sdf.parse(input);
            } catch (ParseException pe) {
                System.out.println("Unable to parse " + input);
                pe.printStackTrace();
            }
        }

        return result;
    }

    private int readInt(String message)  throws IOException {
        int result = Integer.MIN_VALUE;

        while (result == Integer.MIN_VALUE) {
            System.out.println(message);
            String input = scanner.readLine();

            try {
                result = Integer.parseInt(input);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        return result;
    }

    private String readString(String message)  throws IOException {
        System.out.println(message);

        return scanner.readLine();
    }

    private float readfloat(String message)  throws IOException {
        float result = Float.MIN_VALUE;
        System.out.println(message);

        while (result == Integer.MIN_VALUE) {
            System.out.println(message);
            String input = scanner.readLine();

            try {
                result = Float.parseFloat(input);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        return result;
    }

    private float[] readFloatArray(String message) throws IOException {
        List<Float> floats = new ArrayList<>();



        try {
            while (true) {
                System.out.println(message);

                String input = scanner.readLine();

                floats.add(Float.parseFloat(input));
            }
        } catch (NumberFormatException nfe) {
            // safe to ignore
        }

        float[] result = new float[floats.size()];
        for (int i = 0; i < floats.size(); i++) {
            result[i] = floats.get(i);
        }
        return result;
    }


    public void delete() {

    }

    public void deleteRange() {

    }

    public void createNew()  throws IOException {
        Location location = new Location();
        Weather weather = new Weather();
        weather.setId(readInt("Provide an id"));
        weather.setDate(readDate("Provide a date"));

        location.setLat(readfloat("Provide a latitude"));
        location.setLon(readfloat("Provide a longitude"));
        location.setCity(readString("Provide a city"));
        location.setState(readString("Provide a State"));

        weather.setLocation(location);
        weather.setTemperature(readFloatArray("Enter a bunch of floats (enter non-number to end)"));

        String url = "http://" + this.host + "/weather";
        HttpEntity<Weather> request = new HttpEntity<>(weather);
        ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

        System.out.println("Response code: " + response.getStatusCodeValue());
    }

    public void listAll() {
        String url = "http://" + this.host + "/weather";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println("Response code: " + response.getStatusCodeValue());
        System.out.println("Output: "+ response.getBody());
    }

    public static void main(String[] args)  throws IOException {
        WeatherClient client;
        if (args.length > 0) {
            client = new WeatherClient(args[0]);
        } else {
            client = new WeatherClient();
        }
        client.run();
    }
}