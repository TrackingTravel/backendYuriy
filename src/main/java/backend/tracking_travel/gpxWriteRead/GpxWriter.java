package backend.tracking_travel.gpxWriteRead;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class GpxWriter {

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private final File gpxFile;

    public GpxWriter(String path) {
        this.gpxFile = new File(path);
        if (this.gpxFile.isDirectory()) {
            throw new RuntimeException("The given file is a directory.");
        }
    }

    /**
     * Write GPX data
     * <p>
     * The standard GPX is the following tag name, if you have special, please add or modify your own
     *
     * @param track
     */
    public void writeData(Track track) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        String coordsFormat = "%.7f";
        String var5 = "%.1f";
        try {
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();
            Element rootElement = document.createElement("gpx");
            document.appendChild(rootElement);
            Element trk = document.createElement("trk");
            rootElement.appendChild(trk);
            track.getSegments().forEach((s) -> {
                Element trkseg = document.createElement("trkseg");
                trk.appendChild(trkseg);
                s.getPoints().forEach((p) -> {
                    Element trkpt = document.createElement("trkpt");
                    trkpt.setAttribute("lat", String.format(Locale.ROOT, "%.7f", p.getLatitude()));
                    trkpt.setAttribute("lon", String.format(Locale.ROOT, "%.7f", p.getLongitude()));
                    Element time;
                    if (p.getElevation() != null) {
                        time = document.createElement("ele");
                        time.setTextContent(String.format(Locale.ROOT, "%.1f", p.getElevation()));
                        trkpt.appendChild(time);
                    }

                    if (p.getTime() != null) {
                        time = document.createElement("time");
                        time.setTextContent(sdf1.format(p.getTime()));
                        trkpt.appendChild(time);
                    }

                    trkseg.appendChild(trkpt);
                });
            });
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(this.gpxFile);
            transformer.transform(source, console);
            transformer.transform(source, file);
        } catch (TransformerException | ParserConfigurationException var13) {
            var13.printStackTrace();
        }

    }
}

