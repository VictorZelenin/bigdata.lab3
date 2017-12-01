package ua.kpi.cad.lab3.core;

/**
 * This class has all sorts of useful constants predefined for you.
 * These constants have to do with the various discrete values present
 * in the geographic data you are parsing.
 */
public class GeoConstants {
    // Record types

    /**
     * TIGER/Line record type 1 -  Complete Chain Basic Data Record
     */
    public static final String RECORD_TYPE_1 = "1";
    public static final String RT_COMPLETE_CHAIN = RECORD_TYPE_1;

    /**
     * TIGER/Line record type 2 -  Complete Chain Shape Coordinates
     */
    public static final String RECORD_TYPE_2 = "2";
    public static final String RT_CHAIN_SHAPE_COORDINATES = RECORD_TYPE_2;

    // Other TIGER/Line records (we do not currently use these)

    /**
     * Index to Alternate Feature Identifiers
     */
    public static final String RECORD_TYPE_4 = "4";
    /**
     * Index to Alternate Feature Identifiers
     */
    public static final String RECORD_TYPE_5 = "5";
    /**
     * Additional Address Range and ZIP Code Data
     */
    public static final String RECORD_TYPE_6 = "6";
    /**
     * Landmark Features
     */
    public static final String RECORD_TYPE_7 = "7";
    /**
     * Polygons Linked to Area Landmarks
     */
    public static final String RECORD_TYPE_8 = "8";
    /**
     * Polygon Geographic Entity Codes: Current Geography
     */
    public static final String RECORD_TYPE_A = "A";
    /**
     * Polygon Geographic Entity Codes: Corrections
     */
    public static final String RECORD_TYPE_B = "B";
    /**
     * Geographic Entity Names
     */
    public static final String RECORD_TYPE_C = "C";
    /**
     * Polygon Geographic Entity Codes: Economic Census
     */
    public static final String RECORD_TYPE_E = "E";
    /**
     * TIGER/Line ID History
     */
    public static final String RECORD_TYPE_H = "H";
    /**
     * Link Between Complete Chains and Polygons
     */
    public static final String RECORD_TYPE_I = "I";
    /**
     * Feature Spatial Metadata Record
     */
    public static final String RECORD_TYPE_M = "M";
    /**
     * Polygon Internal Point
     */
    public static final String RECORD_TYPE_P = "P";
    /**
     * TIGER/Line ID Record Number Range
     */
    public static final String RECORD_TYPE_R = "R";
    /**
     * Polygon Geographic Entity Codes: Census 2000
     */
    public static final String RECORD_TYPE_S = "S";


    /**
     * BGN Feture Records
     */
    public static final String RECORD_TYPE_BGN = "Z";

    /**
     * Census Population Records
     */
    public static final String RECORD_TYPE_POP = "Y";

	/*
	 * CFCC codes are what identify linear features as roads, highways,
	 * rivers, etc. Some have been aliased as Java constants for your
	 * convenience. CFCC codes consist of 3 characters. The first one
	 * identifies the major feature class (such as road, railroad, etc),
	 * the second one identifies the minor class (for a road this can be
	 * highway vs street) and the third character identifies further
	 * information.
	 *
	 *
	 * For a complete list of CFCCs present in the TIGER/Line data
	 * please refer to this page:
	 * 	http://proximityone.com/tgrcfcc.htm
	 */


    // CFCC Major Classes

    /**
     * Road
     */
    public static final String FEATURE_CLASS_A = "A";
    public static final String FC_ROAD = FEATURE_CLASS_A;

    /**
     * Railroad
     */
    public static final String FEATURE_CLASS_B = "B";
    /**
     * Misc Ground Transport
     */
    public static final String FEATURE_CLASS_C = "C";
    /**
     * Landmark
     */
    public static final String FEATURE_CLASS_D = "D";
    /**
     * Physical Feature
     */
    public static final String FEATURE_CLASS_E = "E";
    /**
     * Nonvisible Feature
     */
    public static final String FEATURE_CLASS_F = "F";
    /**
     * RESERVED
     */
    public static final String FEATURE_CLASS_G = "G";
    /**
     * Hydrography
     */
    public static final String FEATURE_CLASS_H = "H";
    public static final String FC_HYDROGRAPHY = FEATURE_CLASS_H;

    /**
     * Provisional
     */
    public static final String FEATURE_CLASS_P = "P";
    /**
     * Not Yet Classified
     */
    public static final String FEATURE_CLASS_X = "X";

    public static boolean isClass(String featureType, String featureClass) {
        if (featureType == null || featureClass == null)
            return false;
        return featureType.startsWith(featureClass);
    }

    public static String getClass(String featureType) {
        if (featureType == null || featureType.length() == 0)
            return null;
        return featureType.substring(0, 1);
    }

    // CFCC Categories (minor classes)

    // feature class A - road

    /**
     * Primary Highway With Limited Access
     */
    public static final String FEATURE_CLASS_A_CATEGORY_1 = "A1";
    public static final String FCC_ROAD_HGHWY_AND_TOLL = FEATURE_CLASS_A_CATEGORY_1;

    /**
     * Primary Road Without Limited Access
     */
    public static final String FEATURE_CLASS_A_CATEGORY_2 = "A2";
    public static final String FCC_ROAD_HIGHWY_AND_PRIMARY = FEATURE_CLASS_A_CATEGORY_2;

    /**
     * Secondary and Connecting Road
     */
    public static final String FEATURE_CLASS_A_CATEGORY_3 = "A3";
    public static final String FCC_ROAD_SECONDARY_AND_CONNECTING = FEATURE_CLASS_A_CATEGORY_3;

    /**
     * Local, Neighborhood, and Rural Road
     */
    public static final String FEATURE_CLASS_A_CATEGORY_4 = "A4";
    public static final String FCC_ROAD_LOCAL_NBHD_AND_RURAL = FEATURE_CLASS_A_CATEGORY_4;

    /**
     * Vehicular Trail
     */
    public static final String FEATURE_CLASS_A_CATEGORY_5 = "A5";
    /**
     * Road with Special Characteristics
     */
    public static final String FEATURE_CLASS_A_CATEGORY_6 = "A6";
    /**
     * Road as Other Thoroughfare
     */
    public static final String FEATURE_CLASS_A_CATEGORY_7 = "A7";

    // feature class B - railroad

    /**
     * Railroad Main Line
     */
    public static final String FEATURE_CLASS_B_CATEGORY_1 = "B1";
    /**
     * Railroad Spur
     */
    public static final String FEATURE_CLASS_B_CATEGORY_2 = "B2";
    /**
     * Railroad Yard
     */
    public static final String FEATURE_CLASS_B_CATEGORY_3 = "B3";
    /**
     * Railroad with Special Characteristics
     */
    public static final String FEATURE_CLASS_B_CATEGORY_4 = "B4";
    /**
     * Railroad as Other Thoroughfare
     */
    public static final String FEATURE_CLASS_B_CATEGORY_5 = "B5";

	/*
	 * BGN Feature classes identify the types of features present
	 * in BGN records. For a complete list see
	 * 	http://geonames.usgs.gov/domestic/feature_class.htm
	 */

    // BGN Feature class
    public static final String BGN_FEATURE_CLASS_AIRPORT = "airport";
    public static final String BGN_FEATURE_CLASS_BAY = "bay";
    public static final String BGN_FEATURE_CLASS_BRIDGE = "bridge";
    public static final String BGN_FEATURE_CLASS_CANAL = "canal";
    public static final String BGN_FEATURE_CLASS_CIVIL = "civil";
    public static final String BGN_FEATURE_CLASS_ISLAND = "island";
    public static final String BGN_FEATURE_CLASS_LAKE = "lake";
    public static final String BGN_FEATURE_CLASS_PARK = "park";
    public static final String BGN_FEATURE_CLASS_POPULATED_PLACE = "Populated Place";
    public static final String BGN_FEATURE_CLASS_SEA = "sea";

	/*
	 * States are identified in one of two ways in the datasests. Either as
	 * a state abbreviation (as in WA for Washington) or as a FIPS state code
	 * (53 for example is the FIPS code for Washington). We only use the two-
	 * letter abbreviations in our datatypes. Below is a map from FIPS state codes
	 * to 2-letter abbreviations. The index is the FIPS code, and the value
	 * is the state.
	 */


    public static final String FIPS_CODE_NOT_USED = "00";
    // State FIPS code to state 2-letter map
    public static final String[] FIPS_STATE_CODE_MAP = new String[]{
            FIPS_CODE_NOT_USED, // 0
            "AL",
            "AK",
            FIPS_CODE_NOT_USED, // 3
            "AZ",
            "AR",
            "CA",
            FIPS_CODE_NOT_USED, // 7
            "CO",
            "CT",
            "DE",
            "DC",
            "FL",
            "GA",
            FIPS_CODE_NOT_USED, // 14
            "HI",
            "ID",
            "IL",
            "IN",
            "IA",
            "KS",
            "KY",
            "LA",
            "ME",
            "MD",
            "MA",
            "MI",
            "MN",
            "MS",
            "MO",
            "MT",
            "NE",
            "NV",
            "NH",
            "NJ",
            "NM",
            "NY",
            "NC",
            "ND",
            "OH",
            "OK",
            "OR",
            "PA",
            FIPS_CODE_NOT_USED, // 43
            "RI",
            "SC",
            "SD",
            "TN",
            "TX",
            "UT",
            "VT",
            "VA",
            FIPS_CODE_NOT_USED, // 52
            "WA",
            "WV",
            "WI",
            "WY",
            FIPS_CODE_NOT_USED, // 57
            FIPS_CODE_NOT_USED, // 58
            FIPS_CODE_NOT_USED, // 59
            FIPS_CODE_NOT_USED, // 60
            FIPS_CODE_NOT_USED, // 61
            FIPS_CODE_NOT_USED, // 62
            FIPS_CODE_NOT_USED, // 63
            FIPS_CODE_NOT_USED, // 64
            FIPS_CODE_NOT_USED, // 65
            FIPS_CODE_NOT_USED, // 66
            FIPS_CODE_NOT_USED, // 67
            FIPS_CODE_NOT_USED, // 68
            FIPS_CODE_NOT_USED, // 69
            FIPS_CODE_NOT_USED, // 70
            FIPS_CODE_NOT_USED, // 71
            "PR",
            FIPS_CODE_NOT_USED, // 73
            FIPS_CODE_NOT_USED, // 74
            FIPS_CODE_NOT_USED, // 75
            FIPS_CODE_NOT_USED, // 76
            FIPS_CODE_NOT_USED, // 77
            "VI",
    };
}
