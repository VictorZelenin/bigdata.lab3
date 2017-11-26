package ua.kpi.cad.lab3.mapred.mappers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.GeoRecordKey;

import java.io.IOException;

public class RenderingMapper extends Mapper<GeoRecordKey, GeoRecord, IntWritable, GeoRecord> {
    @Override
    protected void map(GeoRecordKey key, GeoRecord value, Context context) throws IOException, InterruptedException {
        // TODO: implement
    }
}
