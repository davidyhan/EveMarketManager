//package dhan.eve.marketmanager.evecentral.quicklook.test;
//
//import static org.junit.Assert.assertNotNull;
//import impl.EveCentral;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import javax.xml.bind.JAXBException;
//
//import org.junit.Test;
//
//import quicklook.EveCentralApi;
//import quicklook.QuickLook;
//
//@SuppressWarnings("restriction")
//public class QuickLookTest {
//
//    @Test
//    public void testQuickLookMarshalling() throws JAXBException, IOException {
//        EveCentral eve = new EveCentral(null);
//
//        String xml = new String(Files.readAllBytes(Paths.get("test/resources/all.txt")));
//
//        EveCentralApi quicklook = eve.unmarshal(xml, EveCentralApi.class);
//
//        assertNotNull(quicklook);
//        assertNotNull(quicklook.getQuick());
//
//        QuickLook quick = quicklook.getQuick();
//        assertNotNull(quick.getBuyOrder());
//        assertNotNull(quick.getSellOrder());
//
//    }
//}
