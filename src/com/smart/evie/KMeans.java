package com.smart.evie;

import java.util.ArrayList;
import java.util.Random;

public class KMeans {
	/* 
	 * K-means implementation for classifying event descriptions into`
	 * unlabeled clusters.
	 */
	private static final int DEFAULT_K = 2;
	
	private int k;
	
	public KMeans() {
		this(DEFAULT_K);
	}
	
	public KMeans(int numClusters) {
		this.k = numClusters;
	}
	
	public ArrayList<double[]> train(ArrayList<double[]> trainingData) {
		/*
		 * Given: ArrayList of feature vectors
		 * Returns: ArrayList of feature vectors where each vector represents the
		 * 	  mean vector corresponding to a cluster. Thus the length of the
		 *    return list is k, or the number of clusters.
		 */
		ArrayList<double[]> means = new ArrayList<double[]>(this.k);
		ArrayList<Integer> clusterSizes = new ArrayList<Integer>(this.k);
		ArrayList<Integer> initSizes = new ArrayList<Integer>(this.k);
		ArrayList<double[]> clusterSums = new ArrayList<double[]>(this.k);
		ArrayList<double[]> initSums = new ArrayList<double[]>(this.k);
		Random rand = new Random();
		boolean stop = false;
		
		// randomly choose k centers and initialize cluster sizes to 0 for all clusters
		for (int i = 0; i < this.k; ++i) {
			int randIndex = rand.nextInt(trainingData.size());
			double[] eventData = trainingData.get(randIndex);
			means.add(eventData);
			initSizes.add(0);
			initSums.add(new double[means.get(i).length]);
		}
		
		while (!stop) {
			clusterSums.clear();
			clusterSizes.clear();
			clusterSizes.addAll(initSizes);
			clusterSums.addAll(initSums);

			// assign training data to a cluster
			for (double[] featureVector : trainingData) {
				int clusterLabel = label(means, featureVector);
				clusterSums.set(clusterLabel, sumVectors(clusterSums.get(clusterLabel), featureVector));
				clusterSizes.set(clusterLabel, clusterSizes.get(clusterLabel) + 1);
			}
			
			/* find the new means and determine if clusters have changed. Might need some epsilon
			 * as a threshold of change if nothing is completely converging. */
			boolean allSame = true;
			for (int clusterIndex = 0; clusterIndex < clusterSums.size(); ++clusterIndex) {
				int clusterSize = clusterSizes.get(clusterIndex);
				double[] clusterMean = divideVector(clusterSums.get(clusterIndex), clusterSize == 0? 1 : clusterSize);
				
				double epsilon = this.euclideanDistance(means.get(clusterIndex), clusterMean);
				if ( epsilon > 3) {
					allSame = false;
				}

				means.set(clusterIndex, clusterMean);
			}
			
			stop = allSame;
		}
		
		return means;
	}
	
	public int label(ArrayList<double[]> means, double[] toTrainFeatures) {
		/*
		 * Given: ArrayList of means where each index has a mean for a separate cluster,
		 *    and a feature vector for the thing you want to label
		 * Return: index of the mean of the cluster the thing you want to label should be in.
		 */
		int clusterLabel = 0;
		double minDistance = Double.MAX_VALUE;
		
		for (int meanIndex = 0; meanIndex  < means.size(); ++meanIndex) {
			double distance = euclideanDistance(means.get(meanIndex), toTrainFeatures);
			if (distance < minDistance) {
				clusterLabel = meanIndex;
				minDistance = distance;
			}
		}
		
		return clusterLabel;
	}
	
	private double euclideanDistance(double[] v1, double[] v2) {
		double sum = 0;
		
		/*
		 *  Want this to throw an exception if the two aren't the same size,
		 *  so have it iterate to the max size of the two vectors
		 */
		for (int i = 0; i < Math.max(v1.length, v2.length); ++i ) {
			sum += Math.pow((v1[i] - v2[i]),2);
		}

		return Math.sqrt(sum);
	}
	
	private double[] sumVectors(double[] v1, double[] v2) {
		double[] sums = new double[Math.max(v1.length, v2.length)];
		
		for (int i = 0; i < sums.length; ++i) {
			sums[i] = v1[i] + v2[i];	
		}
		
		return sums;
	}
	
	private double[] divideVector(double[] v, double scalar) {
		for (int vIndex = 0; vIndex < v.length; ++vIndex) {
			v[vIndex] = v[vIndex]/scalar;
		}
		
		return v;
	}
}
