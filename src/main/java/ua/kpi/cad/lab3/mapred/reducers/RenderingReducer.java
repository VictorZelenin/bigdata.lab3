package ua.kpi.cad.lab3.mapred.reducers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.RenderedTile;
import ua.kpi.cad.lab3.core.protocol.RenderedTileKey;

import java.io.IOException;

public class RenderingReducer extends Reducer<IntWritable, GeoRecord, RenderedTileKey, RenderedTile> {
    @Override
    protected void reduce(IntWritable key, Iterable<GeoRecord> values, Context context) throws IOException, InterruptedException {
        // TODO: implement
    }
}
