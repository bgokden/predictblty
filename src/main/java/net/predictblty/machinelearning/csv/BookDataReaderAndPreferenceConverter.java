package net.predictblty.machinelearning.csv;

import net.predictblty.machinelearning.model.Preference;
import net.predictblty.machinelearning.model.UserPreference;
import org.apache.commons.lang3.math.NumberUtils;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by berkgokden on 1/12/15.
 */
//Data http://www2.informatik.uni-freiburg.de/~cziegler/BX/
public class BookDataReaderAndPreferenceConverter implements DataReader<Preference> {

    @Override
    public List<Preference> read(InputStream is)
            throws Exception {

        Map<String, UserPreference> userPreferenceMap = new HashMap<String, UserPreference>();

        //List<Preference> elements = new LinkedList<Preference>();
        try {
            ICsvListReader reader = new CsvListReader(new InputStreamReader(is), /*CsvPreference.STANDARD_PREFERENCE */CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            String[] tokens = reader.getHeader(true);
            //elements.add(readPreference(tokens, userPreferenceMap));

            int i = 1000;
            List<String> element;
            while ((element = reader.read()) != null && i-->0) {
                tokens = element.toArray(new String[element.size()]);
                readPreference(tokens, userPreferenceMap);
                //elements.add(readPreference(tokens, userPreferenceMap));
            }
        } catch (Exception e) {
            throw e;
        }
        return convertUserPreferencesToPreferences(userPreferenceMap);
    }

    void readPreference(String[] tokens, Map<String, UserPreference> map) {
        Preference preference = new Preference();
        UserPreference userPreference = null;
        int i = 0;
        String userId = null;
        if (tokens[i] != null) {
            userId = tokens[i];
        }
        i++;
        String bookId = null;
        if (tokens[i] != null) {
            bookId = tokens[i];
        }
        i++;
        Double coefficient = null;
        if (tokens[i] != null && NumberUtils.isNumber(tokens[i])) {
            coefficient = Double.parseDouble(tokens[i]);
        }

        userPreference = map.get(userId);
        if (userPreference == null) {
            userPreference = new UserPreference();
        }

        userPreference.setUserId(userId);
        userPreference.addPreference(bookId, coefficient);
        map.put(userId, userPreference);
    }

    public List<Preference> convertUserPreferencesToPreferences(Map<String, UserPreference> userPreferenceMap) {
        Map<String, Preference> preferenceMap = new HashMap<String, Preference>();
        for (UserPreference userPreference : userPreferenceMap.values()) {
            for (Map.Entry<String, Double> stringDoubleEntry : userPreference.getPrefences().entrySet()) {
                String firstKey = stringDoubleEntry.getKey();
                Double firstValue = stringDoubleEntry.getValue();
                for (Map.Entry<String, Double> stringDoubleInnerEntry : userPreference.getPrefences().entrySet()) {
                    String key = null;
                    String secondKey = stringDoubleInnerEntry.getKey();
                    int difference = firstKey.compareTo(stringDoubleInnerEntry.getKey());
                    if (difference > 0) {
                        key = firstKey + "|" + stringDoubleInnerEntry.getValue();
                    } else if (difference < 0) {
                        key = stringDoubleInnerEntry.getValue()+ "|" +firstKey  ;
                    } else {
                        continue;//same keys
                    }
                    Preference preference = preferenceMap.get(key);
                    if (preference == null) {
                        if (firstValue == null || stringDoubleInnerEntry.getValue() == null || Double.isNaN(firstValue) || Double.isNaN(stringDoubleInnerEntry.getValue())) {
                            System.out.println(firstValue+" , "+stringDoubleInnerEntry.getValue());
                        }
                        preference = new Preference(firstKey, secondKey, firstValue*stringDoubleInnerEntry.getValue());
                    }
                    preferenceMap.put(key, preference);
                }
            }
        }
        List<Preference> preferenceList = new ArrayList<Preference>(preferenceMap.size());
        for (Preference preference : preferenceMap.values()) {
            preferenceList.add(preference);
        }
        return preferenceList;
    }
}
