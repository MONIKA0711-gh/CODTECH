package com.recommender;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.recommender.*;

import java.io.File;
import java.util.List;

public class RecommenderApp {

    public static void main(String[] args) throws Exception {
        // Load data from CSV
        DataModel model = new FileDataModel(new File("src/main/java/com/recommender/SampleData.csv"));

        // Use Pearson correlation for similarity
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

        // Find 2 nearest neighbors
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);

        // Create user-based recommender
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        // Recommend top 3 items for user 1
        List<RecommendedItem> recommendations = recommender.recommend(1, 3);

        // Output recommendations
        for (RecommendedItem item : recommendations) {
            System.out.println("Recommended Item ID: " + item.getItemID() + " with value: " + item.getValue());
        }
    }
}
