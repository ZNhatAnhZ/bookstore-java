package util;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class TestUtil {
    private static String getPath(String filename) {
        String path = "src/test/resources/";
        return path+filename;
    }

    public static Properties getCSVData(String filename, String testCaseId) {
        Properties prop = new Properties();
        try {
            InputStream inputStream = new FileInputStream(getPath(filename));
            CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(inputStream)));

            String[] nextLine;
            String[] title = new String[0];

            if ((nextLine = csvReader.readNext()) != null) {
                title = nextLine;
            }
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine[0].equalsIgnoreCase(testCaseId)) {
                    for(int i=0 ; i < title.length; i++) {
                        prop.put(title[i].trim(), nextLine[i].trim());
                    }
                }
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }
}
