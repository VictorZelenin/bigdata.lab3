package ua.kpi.cad.lab3.core;

import ua.kpi.cad.lab3.core.protocol.GeoRecord;

public class GeoRecordCloner {
    public static GeoRecord cloneGeoRecord(GeoRecord geoRecord, String type) {
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
}
