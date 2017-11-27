package ua.kpi.cad.lab3.mapred.reducers;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.RenderedTile;
import ua.kpi.cad.lab3.core.protocol.RenderedTileKey;
import ua.kpi.cad.lab3.mapred.mappers.RenderingMapper;

import java.io.IOException;

public class RenderingReducer extends Reducer<IntWritable, GeoRecord, RenderedTileKey, RenderedTile> {
    @Override
    protected void reduce(IntWritable key, Iterable<GeoRecord> values, Context context) throws IOException, InterruptedException {
        // TODO: implement
    }

    // testing unit
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "TIGER DATA render pass");
        job.setJarByClass(RenderingReducer.class);
        job.setMapperClass(RenderingMapper.class);
//        job.setReducerClass(TigerRecordsJoinerReducer.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(GeoRecord.class);

        job.setOutputKeyClass(RenderedTileKey.class);
        job.setOutputValueClass(RenderedTile.class);


        job.setNumReduceTasks(0);
//        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path("joined"));
        FileOutputFormat.setOutputPath(job, new Path("rendered"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
