package ua.kpi.cad.lab3.mapred.reducers;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.GeoRecordKey;
import ua.kpi.cad.lab3.mapred.mappers.TigerRecordsJoinerMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TigerRecordsJoinerReducer extends Reducer<IntWritable, GeoRecord, GeoRecordKey, GeoRecord> {
    @Override
    protected void reduce(IntWritable key, Iterable<GeoRecord> values, Context context) throws IOException, InterruptedException {
        String featureType = null;

        List<GeoRecord> newRecords = new ArrayList<>();
        for (GeoRecord record : values) {
            if (record.getRecordType().equals("1")) {
                featureType = record.getRecordType1().getFeatureType();
                newRecords.add(cloneGeoRecord(record, record.getRecordType()));
            } else {
                newRecords.add(cloneGeoRecord(record, record.getRecordType()));
            }
        }

        for (GeoRecord record : newRecords) {
            if (record.getRecordType().equals("2")) {
                record.getRecordType2().setFeatureType(featureType);
            }

            context.write(new GeoRecordKey(record.getRecordType()), record);
        }
    }

    private GeoRecord cloneGeoRecord(GeoRecord geoRecord, String type) {

        try {
            GeoRecord newRecord = (GeoRecord) geoRecord.clone();
            if (type.equals("1")) {
                newRecord.setRecordType2(null);
            } else {
                newRecord.setRecordType1(null);
            }

            return newRecord;
        } catch (CloneNotSupportedException e) {
            return geoRecord;
        }
    }

    // testing unit
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "TIGER DATA extract poly features");
        job.setJarByClass(TigerRecordsJoinerReducer.class);
        job.setMapperClass(TigerRecordsJoinerMapper.class);
        job.setReducerClass(TigerRecordsJoinerReducer.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(GeoRecordKey.class);
        job.setOutputValueClass(GeoRecord.class);

        job.setNumReduceTasks(1);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path("filtered"));
        FileOutputFormat.setOutputPath(job, new Path("joined"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
