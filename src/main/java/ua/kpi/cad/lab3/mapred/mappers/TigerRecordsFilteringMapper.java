package ua.kpi.cad.lab3.mapred.mappers;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import ua.kpi.cad.lab3.core.parser.TigerRecordType1Parser;
import ua.kpi.cad.lab3.core.parser.TigerRecordType2Parser;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.GeoRecordKey;

import java.io.IOException;

public class TigerRecordsFilteringMapper extends Mapper<Object, Text, GeoRecordKey, GeoRecord>{

    private final TigerRecordType1Parser parser1;
    private final TigerRecordType2Parser parser2;

    public TigerRecordsFilteringMapper() {
        this.parser1 = new TigerRecordType1Parser();
        this.parser2 = new TigerRecordType2Parser();
    }

    @Override
    protected void map(Object key, Text text, Context context) throws IOException, InterruptedException {
        int recordType = Character.getNumericValue(text.charAt(0));

        GeoRecord geoRecord = parseGeoRecord(text.toString(), recordType);
        geoRecord.setRecordType(String.valueOf(recordType));

        context.write(new GeoRecordKey(recordType), geoRecord);
    }

    private GeoRecord parseGeoRecord(String entry, int recordType) {
        return recordType == 1 ? parser1.parse(entry) : parser2.parse(entry);
    }

    // testing unit
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "TIGER DATA filter pass");
        job.setJarByClass(TigerRecordsFilteringMapper.class);
        job.setMapperClass(TigerRecordsFilteringMapper.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(GeoRecordKey.class);
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(GeoRecordKey.class);
        job.setOutputValueClass(GeoRecord.class);

        job.setNumReduceTasks(0);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path("filtered"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
