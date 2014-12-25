package net.predictblty.machinelearning.csv;

import net.predictblty.machinelearning.model.IrisPlant;
import org.apache.commons.lang3.math.NumberUtils;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by berkgokden on 9/20/14.
 */
public class IrisPlantDataReader implements DataReader<IrisPlant> {

    @Override
    public List<IrisPlant> read(InputStream is)
            throws Exception {

        List<IrisPlant> elements = new LinkedList<IrisPlant>();
        try {
            ICsvListReader reader = new CsvListReader(new InputStreamReader(is), CsvPreference.STANDARD_PREFERENCE /*CsvPreference.EXCEL_PREFERENCE*/);
            String[] tokens = reader.getHeader(true);
            elements.add(readIrisPlant(tokens));

            List<String> element;
            while ((element = reader.read()) != null) {
                tokens = element.toArray(new String[element.size()]);

                elements.add(readIrisPlant(tokens));
            }
        } catch (Exception e) {
            throw e;
        }
        return elements;
    }

    IrisPlant readIrisPlant(String[] tokens) {
        IrisPlant irisPlant = new IrisPlant();

        int i = 0;
        if (tokens[i] != null && NumberUtils.isNumber(tokens[i])) {
            irisPlant.setSepalLength(Double.parseDouble(tokens[i]));
        }
        i++;
        if (tokens[i] != null && NumberUtils.isNumber(tokens[i])) {
            irisPlant.setSepalWidth(Double.parseDouble(tokens[i]));
        }
        i++;
        if (tokens[i] != null && NumberUtils.isNumber(tokens[i])) {
            irisPlant.setPetalLength(Double.parseDouble(tokens[i]));
        }
        i++;
        if (tokens[i] != null && NumberUtils.isNumber(tokens[i])) {
            irisPlant.setPetalWidth(Double.parseDouble(tokens[i]));
        }
        i++;
        if (tokens[i] != null ) {
            irisPlant.setPlantClass(tokens[i]);
        }
        return irisPlant;
    }
}
