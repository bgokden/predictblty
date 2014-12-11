package com.hazelcast.machinelearning.csv;

import com.hazelcast.machinelearning.methods.impl.Classification;
import com.hazelcast.machinelearning.methods.impl.ClassifiedFeatureDatum;
import com.hazelcast.machinelearning.model.IrisPlantClassification;
import com.hazelcast.machinelearning.model.IrisPlantFeature;
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
public class IrisPlantDataReader implements DataReader<ClassifiedFeatureDatum<IrisPlantFeature, String>> {

    @Override
    public List<ClassifiedFeatureDatum<IrisPlantFeature, String>> read(InputStream is)
            throws Exception {

        List<ClassifiedFeatureDatum<IrisPlantFeature, String>> elements = new LinkedList<ClassifiedFeatureDatum<IrisPlantFeature, String>>();
        try (ICsvListReader reader = new CsvListReader(new InputStreamReader(is), CsvPreference.STANDARD_PREFERENCE /*CsvPreference.EXCEL_PREFERENCE*/)) {
            String[] tokens = reader.getHeader(true);
            elements.add(readIrisPlant(tokens));

            List<String> element;
            while ((element = reader.read()) != null) {
                tokens = element.toArray(new String[element.size()]);

                elements.add(readIrisPlant(tokens));
            }
        }
        return elements;
    }

    ClassifiedFeatureDatum<IrisPlantFeature, String> readIrisPlant(String[] tokens) {
        IrisPlantFeature irisPlantFeature = new IrisPlantFeature();
        IrisPlantClassification classification = null;
        if (tokens[0] != null && NumberUtils.isNumber(tokens[0])) {
            irisPlantFeature.setSepalLength(Double.parseDouble(tokens[0]));
        }
        if (tokens[1] != null && NumberUtils.isNumber(tokens[1])) {
            irisPlantFeature.setSepalWidth(Double.parseDouble(tokens[1]));
        }
        if (tokens[2] != null && NumberUtils.isNumber(tokens[2])) {
            irisPlantFeature.setPetalLength(Double.parseDouble(tokens[2]));
        }
        if (tokens[3] != null && NumberUtils.isNumber(tokens[3])) {
            irisPlantFeature.setPetalWidth(Double.parseDouble(tokens[3]));
        }
        if (tokens[4] != null ) {
            //irisPlant.setPlantClass(tokens[4]);
            classification = new IrisPlantClassification(tokens[4]);
        }
        return new ClassifiedFeatureDatum<IrisPlantFeature, String>(irisPlantFeature,classification);
    }
}
