package backend.tracking_travel.gpxWriteRead;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GpxReader {

    // Here the time format Select OK according to the time format in the GPX file.
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private final File gpxFile;

    public GpxReader(String path) {

        this.gpxFile = new File(path);

        if (!this.gpxFile.exists()) {

            throw new RuntimeException("File " + path + "does not exist.");

        } else if (this.gpxFile.isDirectory()) {

            throw new RuntimeException("The given file is a directory.");
        }
    }

    /**
     * Read trajectory data
     * <p>
     * The standard GPX is the following tag name, if you have special, please add or modify your own
     *
     * @return
     */
    public Track readData() {
        Track track = new Track();
        try {
            // 1, get the factory example of the DOM parser
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            // 2, then get a DOM parser from the DOM factory
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // 3, convert the GPX document to be parsed into input stream so that the DOM parser parsing it
            Document document = dBuilder.parse(this.gpxFile);
            // 4, find each byage point (you can also understand it as root node)
            // Get the specified node
            // Need data in the TRK tag
            NodeList trk = document.getElementsByTagName("trk");
            for (int i = 0; i < trk.getLength(); ++i) {
                Node trkItem = trk.item(i);
                if (trkItem.getNodeName().equals("trk")) {
                    NodeList trkSegments = trkItem.getChildNodes();
                    for (int j = 0; j < trkSegments.getLength(); ++j) {
                        Node trkSegment = trkSegments.item(j);
                        if (trkSegment.getNodeName().equals("trkseg")) {
                            TrackSegment segment = new TrackSegment();
                            track.addTrackSegment(segment);
                            // Get its corresponding word node
                            NodeList trkPts = trkSegment.getChildNodes();
                            for (int k = 0; k < trkPts.getLength(); ++k) {
                                Node trkPt = trkPts.item(k);
                                String nodename = trkPt.getNodeName();
                                if (trkPt.getNodeName().equals("trkpt")) {
                                    Element element = (Element) trkPt;
                                    double lat = Double.parseDouble(element.getAttribute("lat"));
                                    double lon = Double.parseDouble(element.getAttribute("lon"));
                                    Double ele = null;
                                    String time = null;
                                    // Get its corresponding word node
                                    List<Node> nodes = toNodeList(element.getChildNodes());
                                    Optional<Node> elev = nodes.stream().filter((e) -> {
                                        return e.getNodeName().equals("ele");
                                    }).findFirst();
                                    if (elev.isPresent()) {
                                        ele = Double.parseDouble(((Node) elev.get()).getTextContent());
                                    }
                                    Optional<Node> timeNode = nodes.stream().filter((e) -> {
                                        return e.getNodeName().equals("time");
                                    }).findFirst();
                                    if (timeNode.isPresent()) {
                                        time = ((Node) timeNode.get()).getTextContent();
                                    }
                                    segment.addTrackPoint(new TrackPoint(lat, lon, ele, this.parseDate(time)));
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException var26) {
            var26.printStackTrace();
        }

        return track;
    }

    private static List<Node> toNodeList(NodeList nodeList) {
        List<Node> nodes = new ArrayList();

        for (int i = 0; i < nodeList.getLength(); ++i) {
            nodes.add(nodeList.item(i));
        }

        return nodes;
    }

    private Date parseDate(String value) {
        Date date = null;
        try {
            date = sdf1.parse(value);
        } catch (ParseException var5) {

        }
        return date;
    }
}

