package net.predictblty.machinelearning.mlalgorithm.mlalgorithmimpl;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import net.predictblty.machinelearning.ToStringPrettyfier;
import net.predictblty.machinelearning.csv.BookDataReaderAndPreferenceConverter;
import net.predictblty.machinelearning.csv.IrisPlantDataReader;
import net.predictblty.machinelearning.csv.PreferenceDataReader;
import net.predictblty.machinelearning.mlalgorithm.MLAlgorithm;
import net.predictblty.machinelearning.mlcommon.Classification;
import net.predictblty.machinelearning.mlcommon.FeatureComparators;
import net.predictblty.machinelearning.mlcommon.HelpfulMethods;
import net.predictblty.machinelearning.model.IrisPlant;
import net.predictblty.machinelearning.model.Preference;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.*;

public class UserBasedCollaborativeFilteringRecommandationAlgorithmTest {
    @Test
    public void shouldPassWhenSuccessIsHigh() throws Exception {
        Hazelcast.shutdownAll();
        // Prepare Hazelcast cluster
        HazelcastInstance hazelcastInstance = HelpfulMethods.buildCluster(1);

        System.out.println("Cluster ready");

        try {
            //Read data
            BookDataReaderAndPreferenceConverter preferenceDataReader = new BookDataReaderAndPreferenceConverter();
            InputStream is = UserBasedCollaborativeFilteringRecommandationAlgorithmTest.class.getClassLoader().getResourceAsStream("BX-Book-Ratings.csv");//TODO: add user data
            List<Preference> preferenceList = preferenceDataReader.read(is);

            //Prepare training data and prediction data


            //70% for training 30% for prediction
            int trainSize = (int) Math.round(0.7 * preferenceList.size());
            int predictSize = preferenceList.size() - trainSize;
            Set<Integer> predictDataIndex = new HashSet<Integer>();
            Random rd = new Random();
            for (int i = 0; i < predictSize; i++) {
                while ( predictDataIndex.add(rd.nextInt(preferenceList.size())) == false) {}
            }
            Collection<Preference> trainData = new ArrayList<Preference>(trainSize);
            for (int i = 0; i < preferenceList.size(); i++) {
                if (!predictDataIndex.contains(i)) {
                    trainData.add(preferenceList.get(i));
                }
            }

            Map<String, Object> options = new HashMap<String, Object>();
            options.put("comparator", new FeatureComparators.DoubleManhattanDistanceFeatureComparator());
            MLAlgorithm algorithm = new UserBasedCollaborativeFilteringRecommandationAlgorithm(hazelcastInstance, options);
            algorithm.train(trainData);

            int success = 0;

            for (Integer integer : predictDataIndex) {
                Preference preferenceToPredict = preferenceList.get(integer.intValue());
                Collection<Preference> predictData = new ArrayList<Preference>(1);
                predictData.add(new Preference(preferenceToPredict.getPreferenceKey(), preferenceToPredict.getPrefenceCoefficient()));
                Collection<Classification> classifications = algorithm.predict(predictData);
                double successPartial = HelpfulMethods.compareClassificationsWithClass(classifications, preferenceToPredict.getPreferenceKey());
                System.out.println("Result: " + successPartial);
                success+=successPartial;
                if (successPartial <= 0.5) {
                    System.out.println("Class to predict: " + preferenceToPredict.getPrefenceValue());
                    System.out.println("Result: " + ToStringPrettyfier.toString(classifications));
                }
            }
            double successRate = ((double) success) / predictDataIndex.size();
            System.out.println("Success Rate: " + successRate);
            //Assert.assertTrue(successRate >= 0.6); //TODO: I don't know how to measure recommendation
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            // Shutdown cluster
            Hazelcast.shutdownAll();
        }
    }


    @Test
    public void shouldPassWhenSuccessIsHighSimple() throws Exception {
        Hazelcast.shutdownAll();
        // Prepare Hazelcast cluster
        HazelcastInstance hazelcastInstance = HelpfulMethods.buildCluster(1);

        System.out.println("Cluster ready");

        try {


            Map<String, Object> options = new HashMap<String, Object>();
            options.put("comparator", new FeatureComparators.DoubleManhattanDistanceFeatureComparator());
            MLAlgorithm algorithm = new UserBasedCollaborativeFilteringRecommandationAlgorithm(hazelcastInstance, options);
            Collection<Preference> trainData = new LinkedList<Preference>();
            trainData.add(new Preference("A","B", 1.0));
            trainData.add(new Preference("A","C", 1.0));
            trainData.add(new Preference("A","B", 3.0));
            trainData.add(new Preference("D","B", 0.5));

            algorithm.train(trainData);


            Collection<Preference> predictData = new LinkedList<Preference>();
            predictData.add(new Preference("B", 1.0));
            Collection<Classification> classifications = algorithm.predict(predictData);

            System.out.println(ToStringPrettyfier.toString(classifications));

        } catch (Exception e){
            e.printStackTrace();
        }finally {
            // Shutdown cluster
            Hazelcast.shutdownAll();
        }
    }

}