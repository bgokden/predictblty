package com.hazelcast.machinelearning.csv;

import com.hazelcast.machinelearning.model.IrisPlant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
        } catch (Exception e) {}
        return elements;
    }

    IrisPlant readIrisPlant(String[] tokens) {
        IrisPlant irisPlant = new IrisPlant();

        if (tokens[0] != null && NumberUtils.isNumber(tokens[0])) {
            irisPlant.setSepalLength(Double.parseDouble(tokens[0]));
        }
        if (tokens[1] != null && NumberUtils.isNumber(tokens[1])) {
            irisPlant.setSepalWidth(Double.parseDouble(tokens[1]));
        }
        if (tokens[2] != null && NumberUtils.isNumber(tokens[2])) {
            irisPlant.setPetalLength(Double.parseDouble(tokens[2]));
        }
        if (tokens[3] != null && NumberUtils.isNumber(tokens[3])) {
            irisPlant.setPetalWidth(Double.parseDouble(tokens[3]));
        }
        if (tokens[4] != null ) {
            irisPlant.setPlantClass(tokens[4]);
        }
        return irisPlant;
    }
}
