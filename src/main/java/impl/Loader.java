package impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import EveApi.CharOrder;

public class Loader {

    public static void main(String[] args) throws Exception {
        String file = "C:\\Users\\David\\workspace\\EveMarketManager\\charOrder.txt";
        String fullURL = "https://api.eveonline.com/char/MarketOrders.xml.aspx?keyID=4102574&vCode=bRm8i16U0kHCsI0mxHfsH28tgXQB65TRQ2eDijUkeOgAxW7jQPgrwhoNims1G49f";
        EveCentral eve = new EveCentral(EveCentral.marketStatBase);

        String fileString = readFile(file, StandardCharsets.UTF_8);

        System.out.println(fileString);

        CharOrder order = eve.unmarshal(fileString, CharOrder.class);

        System.out.println(order.toString());
    }

    public static String queryEveCentralUrl(String fullURL) throws IOException {
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

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
