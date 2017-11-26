package ua.kpi.cad.lab3.mapred.mappers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import ua.kpi.cad.lab3.core.GeoConstants;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.GeoRecordKey;
import ua.kpi.cad.lab3.core.protocol.TigerRecordType1;

import java.io.IOException;

public class TigerRecordsJoinerMapper extends Mapper<GeoRecordKey, GeoRecord, IntWritable, GeoRecord> {
    @Override
    protected void map(GeoRecordKey key, GeoRecord value, Context context) throws IOException, InterruptedException {
        System.out.println();
//        if (key.getRecordType().startsWith(GeoConstants.RECORD_TYPE_1)) {
//            context.write(new IntWritable(value.getRecordType1().getLineId()), value);
//        } else {
//            context.write(new IntWritable(value.getRecordType2().getLineId()), value);
//        }
    }
}
