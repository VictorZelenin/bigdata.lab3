package ua.kpi.cad.lab3.mapred;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import ua.kpi.cad.lab3.core.protocol.GeoRecord;
import ua.kpi.cad.lab3.core.protocol.GeoRecordKey;

import java.io.IOException;

public class TigerRecordsFilteringMapper extends Mapper<Object, Text, GeoRecordKey, GeoRecord>{

    @Override
    protected void map(Object key, Text text, Context context) throws IOException, InterruptedException {
        // 1. split text to lines
        // 2. for each line :
        //    - return type of record
        //    - according to record type pick appropriate parser
        // 3. parse TigerRecord
        // 4. wrap TigerRecord into GeoRecord
        // 5. pass result to next step
    }
}
