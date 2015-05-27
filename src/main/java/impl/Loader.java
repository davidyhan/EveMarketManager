package impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import EveApi.EveApi;

public class Loader {
    private final static String NarwhalApi = "https://api.eveonline.com/char/MarketOrders.xml.aspx?keyID=4411599&vCode=M92INSxszKofWhN02pVpla8QO1yl76It197OSMeZ8BTrcy33QZ3EjZ4QUkBoKsAt";
    private static EveCentral eveCent = new EveCentral("");

    // Testing new code
    public static void main(String[] args) throws Exception {

        EveApi api = eveCent.unmarshal(queryURL(NarwhalApi), EveApi.class);

        System.out.println(api.toString());
    }

    public static String queryURL(String fullURL) throws IOException {
        URL obj = new URL(fullURL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
