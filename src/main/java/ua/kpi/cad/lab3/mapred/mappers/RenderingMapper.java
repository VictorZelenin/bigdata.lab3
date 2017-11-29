package ua.kpi.cad.lab3.mapred.mappers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import ua.kpi.cad.lab3.core.GeoConstants;
import ua.kpi.cad.lab3.core.GeoRecordCloner;
import ua.kpi.cad.lab3.core.divider.SimpleDivider;
import ua.kpi.cad.lab3.core.divider.TileSetDivider;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.GeoRecordKey;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RenderingMapper extends Mapper<GeoRecordKey, GeoRecord, IntWritable, GeoRecord> {

    private TileSetDivider divider;

    @Override
    protected void map(GeoRecordKey key, GeoRecord value, Context context) throws IOException, InterruptedException {
        // TODO: implement
        createDivider(context);
        Set<IntWritable> passedSetIds = new HashSet<>();
        if (value.getRecordType().startsWith(GeoConstants.RECORD_TYPE_1)) {
            int startTileSetId = divider.getTileSetId(
                    divider.getTileID(value.getRecordType1().getStartLat(),
                            value.getRecordType1().getStartLong())
            );
            int endTileSetId = divider.getTileSetId(
                    divider.getTileID(value.getRecordType1().getEndLat(),
                            value.getRecordType1().getEndLong())
            );
            if (startTileSetId == endTileSetId) {
                context.write(new IntWritable(startTileSetId), GeoRecordCloner.cloneGeoRecord(value, "1"));
            } else {
                context.write(new IntWritable(startTileSetId), GeoRecordCloner.cloneGeoRecord(value, "1"));
                context.write(new IntWritable(endTileSetId), GeoRecordCloner.cloneGeoRecord(value, "1"));
            }
        } else {
            double[] listLatitudes = value.getRecordType2().getListLat();
            double[] listLongitude = value.getRecordType2().getListLong();
            for (int pointNum = 0; pointNum < listLatitudes.length; pointNum++) {
                TileSetDivider.TileID tileID =
                        divider.getTileID(
                                listLatitudes[pointNum],
                                listLongitude[pointNum]
                        );
                IntWritable currentTileSetId = new IntWritable(divider.getTileSetId(tileID));
                if (!passedSetIds.contains(currentTileSetId)) {
                    passedSetIds.add(currentTileSetId);
                    context.write(currentTileSetId, GeoRecordCloner.cloneGeoRecord(value, "2"));
                }
            }
        }
    }

    private void createDivider(Context context) {
        divider = new SimpleDivider(47.084457, -122.541068, 47.780328, -121.065709, 1);
        divider.assignTileSetIds(10);
    }
}
