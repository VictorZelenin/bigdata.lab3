package ua.kpi.cad.lab3.main;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import ua.kpi.cad.lab3.core.GeoConstants;
import ua.kpi.cad.lab3.core.divider.TileSetDivider;
import ua.kpi.cad.lab3.core.parser.TigerRecordType1Parser;
import ua.kpi.cad.lab3.core.parser.TigerRecordType2Parser;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.renderer.TIleRendererImpl;
import ua.kpi.cad.lab3.core.renderer.TileRenderer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main implements Serializable {
    private static final String MASTER = "local[*]";

    private final TigerRecordType1Parser parser1 = new TigerRecordType1Parser();
    private final TigerRecordType2Parser parser2 = new TigerRecordType2Parser();

    private final TileSetDividerSparkImpl divider =
            new TileSetDividerSparkImpl(47, -122, 48, -122, 3);
    private final TileRenderer renderer = new TIleRendererImpl();

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Please, specify input path.");
        }

        String in = args[0];

        new Main().run(in);
    }

    private void run(String in) {
        SparkConf conf = new SparkConf()
                .setAppName("Big Data, Lab3")
                .setMaster(MASTER);

        JavaSparkContext context = new JavaSparkContext(conf);

        divider.assignTileSetIds(10);
        context.textFile(in)
                .map(this::filterData)
                .groupBy(record -> record.getRecordType().startsWith(GeoConstants.RECORD_TYPE_1) ?
                    record.getRecordType1().getLineId() : record.getRecordType2().getLineId())
                .flatMap(grouped -> joinRecords(grouped._2).iterator())
                .flatMap(record -> groupByCoords(record).iterator())
                .groupBy(tuple -> tuple._1)
                .map(t -> t._2)
                .foreach(this::doRender);
    }

    private GeoRecord filterData(String text) {
        int recordType = Character.getNumericValue(text.charAt(0));

        GeoRecord geoRecord = parseGeoRecord(text, recordType);
        geoRecord.setRecordType(String.valueOf(recordType));

        return geoRecord;
    }

    private List<GeoRecord> joinRecords(Iterable<GeoRecord> records) {
        List<GeoRecord> joinedRecords = new ArrayList<>();

        String featureType = null;

        for (GeoRecord geoRecord : records) {
            if (geoRecord.getRecordType().equals("1")) {
                featureType = geoRecord.getRecordType1().getFeatureType();
                joinedRecords.add(geoRecord);
            }
        }

        for (GeoRecord geoRecord : records) {
            if (geoRecord.getRecordType().equals("2")) {
                geoRecord.getRecordType2().setFeatureType(featureType);
                joinedRecords.add(geoRecord);
            }
        }

        return joinedRecords;
    }

    private List<Tuple2<Integer, GeoRecord>> groupByCoords(GeoRecord record) {
        Set<Integer> passedSetIds = new HashSet<>();

        List<Tuple2<Integer, GeoRecord>> groupedRecords = new ArrayList<>();
        if (record.getRecordType().startsWith(GeoConstants.RECORD_TYPE_1)) {
            int startTileSetId = divider.getTileSetId(divider.getTileID(record.getRecordType1().getStartLat(),
                    record.getRecordType1().getStartLong())
            );
            int endTileSetId = divider.getTileSetId(divider.getTileID(record.getRecordType1().getEndLat(),
                            record.getRecordType1().getEndLong())
            );
            if (startTileSetId == endTileSetId) {
                groupedRecords.add(new Tuple2<>(startTileSetId, record));
            } else {
                groupedRecords.add(new Tuple2<>(startTileSetId, record));
                groupedRecords.add(new Tuple2<>(endTileSetId, record));
            }
        } else {
            double[] listLatitudes = record.getRecordType2().getListLat();
            double[] listLongitude = record.getRecordType2().getListLong();

            for (int pointNum = 0; pointNum < listLatitudes.length; pointNum++) {
                TileSetDivider.TileID tileID =
                        divider.getTileID(
                                listLatitudes[pointNum],
                                listLongitude[pointNum]
                        );
                Integer currentTileSetId = divider.getTileSetId(tileID);
                if (!passedSetIds.contains(currentTileSetId)) {
                    passedSetIds.add(currentTileSetId);
                    groupedRecords.add(new Tuple2<>(currentTileSetId, record));
                }
            }
        }

        return groupedRecords;
    }

    private void doRender(Iterable<Tuple2<Integer, GeoRecord>> values) {
        Integer tileSetId = values.iterator().next()._1;

        for (Tuple2<Integer, GeoRecord> value : values) {
            renderer.addRecord(value._2);
        }

        try {
            divider.renderTileSet(renderer, tileSetId);
        } catch (IOException | InterruptedException e) {
//             ignored
        }
    }

    private GeoRecord parseGeoRecord(String entry, int recordType) {
        return recordType == 1 ? parser1.parse(entry) : parser2.parse(entry);
    }
}