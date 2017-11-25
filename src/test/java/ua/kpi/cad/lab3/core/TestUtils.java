package ua.kpi.cad.lab3.core;

import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Util class for unit-tests.
 */
public class TestUtils {

    public static List<String> readFileByLines(String filename) throws IOException {
        return splitRawContentStringIntoLines(readFileAsRawString(filename));
    }

    private static String readFileAsRawString(String filename) throws IOException {
        URL testResource = Resources.getResource(filename);

        BufferedInputStream content = (BufferedInputStream) testResource.getContent();

        return IOUtils.toString(content);
    }

    private static List<String> splitRawContentStringIntoLines(String rawContent) {
        return Arrays.asList(rawContent.split("\n"));
    }
}
