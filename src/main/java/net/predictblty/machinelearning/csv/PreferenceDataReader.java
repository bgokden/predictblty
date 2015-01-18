package net.predictblty.machinelearning.csv;

import net.predictblty.machinelearning.model.IrisPlant;
import net.predictblty.machinelearning.model.Preference;
import org.apache.commons.lang3.math.NumberUtils;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by berkgokden on 1/12/15.
 */
public class PreferenceDataReader  implements DataReader<Preference> {

    @Override
    public List<Preference> read(InputStream is)
            throws Exception {

        List<Preference> elements = new LinkedList<Preference>();
        try {
            ICsvListReader reader = new CsvListReader(new InputStreamReader(is), CsvPreference.STANDARD_PREFERENCE /*CsvPreference.EXCEL_PREFERENCE*/);
            String[] tokens = reader.getHeader(true);
            //elements.add(readPreference(tokens));

            List<String> element;
            while ((element = reader.read()) != null) {
                tokens = element.toArray(new String[element.size()]);

                elements.add(readPreference(tokens));
            }
        } catch (Exception e) {
            throw e;
        }
        return elements;
    }

    Preference readPreference(String[] tokens) {
        Preference preference = new Preference();

        int i = 0;
        if (tokens[i] != null) {
            preference.setPreferenceKey(tokens[i]);
        }
        i++;
        if (tokens[i] != null) {
            preference.setPrefenceValue(tokens[i]);
        }
        i++;
        if (tokens[i] != null && NumberUtils.isNumber(tokens[i])) {
            preference.setPrefenceCoefficient(Double.parseDouble(tokens[i]));
        }
        return preference;
    }
}