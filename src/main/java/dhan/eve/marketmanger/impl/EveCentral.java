package dhan.eve.marketmanger.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import dhan.eve.marketmanager.evecentral.marketstat.MarketApi;

@SuppressWarnings("restriction")
public class EveCentral {
    public static final String quickLookBase = "http://api.eve-central.com/api/quicklook";
    public static final String marketStatBase = "http://api.eve-central.com/api/marketstat";

    String url;

    public EveCentral(String queryURL) {
        url = queryURL;
    }

    // querys a single item type in a single system
    public String queryItemBySystem(int item, int system) throws IOException {
        String queryURL = url + "?typeid=" + item + "&usesystem=" + system;

        return queryEveCentralUrl(queryURL);
    }
    
    //Querys a list of items from a list of regions
    public String queryItemsByRegions(List<Integer> items, List<Integer> regions) throws IOException {
        String queryURL = url;

        // Adds the list of items to the query url
        for (int i = 0; i < items.size(); i++) {
            if (i == 0) {
                queryURL += "?typeid=" + items.get(i);
            } else {
                queryURL += "&typeid=" + items.get(i);
            }
        }
        
        for(Integer region: regions){
            queryURL+= "&regionlimit=" + region;
        }
        
        return queryEveCentralUrl(queryURL);
    }

    // querys a list of items in a single system
    public String queryItemsBySystem(List<Integer> items, int system) throws IOException {
        String queryURL = url;

        // Adds the list of items to the query url
        for (int i = 0; i < items.size(); i++) {
            if (i == 0) {
                queryURL += "?typeid=" + items.get(i);
            } else {
                queryURL += "&typeid=" + items.get(i);
            }
        }

        queryURL += "&usesystem=" + system;

        return queryEveCentralUrl(queryURL);
    }

    public MarketApi queryMinerals(int system) throws IOException, JAXBException {
        String allMineralUrl = url + "?typeid=34&typeid=35&typeid=36&typeid=37&typeid=38&typeid=39&typeid=40&usesystem=" + system;

        String queryReturn = queryEveCentralUrl(allMineralUrl);

        MarketApi minerals = unmarshal(queryReturn, MarketApi.class);

        return minerals;
    }

    public String queryEveCentralUrl(String fullURL) throws IOException {
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

    @SuppressWarnings("unchecked")
    public <T> T unmarshal(String xml, Class<T> clazz) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        Object unmarshalled = jaxbUnmarshaller.unmarshal(new StringReader(xml));

        return (T) unmarshalled;
    }
}
