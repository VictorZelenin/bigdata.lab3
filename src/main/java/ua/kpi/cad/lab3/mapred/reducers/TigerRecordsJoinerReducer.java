package ua.kpi.cad.lab3.mapred.reducers;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.mapred.mappers.TigerRecordsFilteringMapper;
import ua.kpi.cad.lab3.mapred.mappers.TigerRecordsJoinerMapper;

import java.io.IOException;

public class TigerRecordsJoinerReducer extends Reducer<IntWritable, GeoRecord, IntWritable, GeoRecord> {
    @Override
    protected void reduce(IntWritable key, Iterable<GeoRecord> values, Context context) throws IOException, InterruptedException {
        System.out.println();
        super.reduce(key, values, context);
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
//        TigerRecordsFilteringMapper.main(args);

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "TIGER DATA extract poly features");
        job.setJarByClass(TigerRecordsJoinerReducer.class);
        job.setMapperClass(TigerRecordsJoinerMapper.class);
        job.setReducerClass(TigerRecordsJoinerReducer.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(GeoRecord.class);

        job.setNumReduceTasks(1);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path("filtered"));
        FileOutputFormat.setOutputPath(job, new Path("joined"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
