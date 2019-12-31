package exam.sd.weather.client;

import exam.sd.weather.bean.Location;
import exam.sd.weather.bean.Weather;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            Long result = readLong("");
            if (result != null) {
                input = result.intValue();
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
    }

    private LocalDate readDate(String message)  throws IOException {
        LocalDate result = null;
        String dateFormat = "yyyy-MM-dd";
        String input = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        while(result == null) {
            System.out.println(message + "(" + dateFormat + ")");
            try {
                input = scanner.readLine();

                result = LocalDate.parse(input, formatter);
            } catch (DateTimeParseException pe) {
                System.out.println("Unable to parse " + input);
                pe.printStackTrace();
            }
        }

        return result;
    }

    private Long readLong(String message)  throws IOException {
        Long result = null;

        while (result == null) {
            System.out.println(message);
            String input = scanner.readLine();

            if (input.trim().toLowerCase().equals("n")) {
                return null;
            }
            try {
                result = Long.parseLong(input);
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

    private BigDecimal readBigDecimal(String message)  throws IOException {
        BigDecimal result = null;

        while (result == null) {
            System.out.println(message);
            String input = scanner.readLine();

            try {
                result = new BigDecimal(input);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        return result;
    }

    private List<BigDecimal> readBigDecimalList(String message) throws IOException {
        List<BigDecimal> floats = new ArrayList<>();

        try {
            while (true) {
                System.out.println(message);

                String input = scanner.readLine();

                floats.add(new BigDecimal(input));
            }
        } catch (NumberFormatException nfe) {
            // safe to ignore
        }

        if (floats.size() < Weather.TEMPERATURE_COUNT) {
            System.out.println("Would you like to backfill the array with the last number provided? (y/n)");

            String input = scanner.readLine();
            if (input.toLowerCase().equals("y")) {
                BigDecimal v = floats.get(floats.size() - 1);
                while (floats.size() < Weather.TEMPERATURE_COUNT) {
                    floats.add(v);
                }
            }
        }

        return floats;
    }


    public void delete() {
        String url = "http://" + this.host + "/erase";
        try {
            restTemplate.delete(url, new HashMap<>());
        } catch (HttpClientErrorException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getResponseBodyAsString());
            ex.printStackTrace();
        }
    }

    public void deleteRange() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = readDate("Provide start");
        LocalDate end = readDate("Provide end");

        BigDecimal lat = readBigDecimal("Provide lat");
        BigDecimal lon = readBigDecimal("Provide lon");

        String url = String.format("http://%s/erase?start=%s&end=%s&lat=%s&lon=%s",
                this.host, formatter.format(start), formatter.format(end), lat, lon);
        try {
            restTemplate.delete(url, new HashMap<>());
        } catch (HttpClientErrorException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getResponseBodyAsString());
            ex.printStackTrace();
        }
    }

    public void createNew()  throws IOException {
        Location location = new Location();
        Weather weather = new Weather();
        weather.setId(readLong("Provide an id"));
        weather.setDate(readDate("Provide a date"));

        location.setLat(readBigDecimal("Provide a latitude"));
        location.setLon(readBigDecimal("Provide a longitude"));
        location.setCity(readString("Provide a city"));
        location.setState(readString("Provide a State"));

        weather.setLocation(location);
        weather.setTemperature(readBigDecimalList("Enter a bunch of floats (enter non-number to end)"));

        String url = "http://" + this.host + "/weather";
        HttpEntity<Weather> request = new HttpEntity<>(weather);
        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            System.out.println("Response code: " + response.getStatusCodeValue());
        } catch (HttpClientErrorException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getResponseBodyAsString());
            ex.printStackTrace();
        }
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
