package com.smart.evie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {
	/* 
	 * K-means implementation for classifying event descriptions into
	 * unlabelled clusters.
	 */
	
	private int k;
	
	public KMeans(int numClusters) {
		this.k = numClusters;
	}
	
	public ArrayList<List<Double>> train(ArrayList<List<Double>> trainingData) {
		/*
		 * Given: ArrayList of feature vectors
		 * Returns: ArrayList of feature vectors where each vector represents the
		 * 	  mean vector corresponding to a cluster. Thus the length of the
		 *    return list is k, or the number of clusters.
		 */
		ArrayList<List<Double>> means = new ArrayList<List<Double>>(this.k);
		ArrayList<Integer> initSizes = new ArrayList<Integer>(this.k);
		ArrayList<List<Double>> clusterSums = new ArrayList<List<Double>>(this.k);
		ArrayList<Integer> clusterSizes = new ArrayList<Integer>(this.k);
		ArrayList<List<Double>> initSums = new ArrayList<List<Double>>(this.k);
		Random rand = new Random();
		boolean stop = false;
		
		// randomly choose k centers and initialize cluster sizes to 1 for all clusters
		for (int i = 0; i < means.size(); ++i) {
			means.add(trainingData.get(rand.nextInt(trainingData.size())));
			initSizes.add(1);
		}
		
		/* Somewhere here, initialize initSums to be ArrayList of List<Double>, where
		 * the ArrayList has length k, and List<Double> at index i is just list of zeros
		 * with length means.get(i).size()*/
		
		while (!stop) {
			clusterSums.clear();
			clusterSizes.clear();
			clusterSizes.addAll(initSizes);
			clusterSums.addAll(initSums);
			
			// assign training data to a cluster
			for (List<Double> featureVector : trainingData) {
				int clusterLabel = label(means, featureVector);
				clusterSums.set(clusterLabel, sumVectors(clusterSums.get(clusterLabel), featureVector));
				clusterSizes.set(clusterLabel, clusterSizes.get(clusterLabel) + 1);
			}
			
			/* find the new means and determine if clusters have changed. Might need some epsilon
			 * as a threshold of change if nothing is completely converging. */
			boolean allSame = true;
			for (int clusterIndex = 0; clusterIndex < clusterSums.size(); ++clusterIndex) {
				List<Double> clusterMean = divideVector(clusterSums.get(clusterIndex), clusterSizes.get(clusterIndex));
				
				if (!means.get(clusterIndex).equals(clusterMean)) {
					allSame = false;
				}
				
				means.set(clusterIndex, clusterMean);
			}
			
			stop = allSame;
		}
		
		return means;
	}
	
	public int label(ArrayList<List<Double>> means, List<Double> toTrainFeatures) {
		/*
		 * Given: ArrayList of means where each index has a mean for a separate cluster,
		 *    and a feature vector for the thing you want to label
		 * Return: index of the mean of the cluster the thing you want to label should be in.
		 */
		int label = 0;
		double minDistance = 0;
		
		for (int meanIndex = 0; meanIndex  < means.size(); ++meanIndex) {
			double distance = euclideanDistance(means.get(meanIndex), toTrainFeatures);
			if (distance < minDistance) {
				label = meanIndex;
				minDistance = distance;
			}
		}
		
		return label;
	}
	
	private double euclideanDistance(List<Double> v1, List<Double> v2) {
		double sum = 0;
		
		/*
		 *  Want this to throw an exception if the two aren't the same size,
		 *  so have it iterate to the max size of the two vectors
		 */
		for (int i = 0; i < Math.max(v1.size(), v2.size()); ++i ) {
			sum += Math.pow((v1.get(i) - v2.get(i)),2);
		}
		
		return Math.sqrt(sum);
	}
	
	private List<Double> sumVectors(List<Double> v1, List<Double> v2) {
		List<Double> sums = new ArrayList<Double>(v1.size());
		
		for (int i = 0; i < Math.max(v1.size(), v2.size()); ++i) {
			sums.add(v1.get(i) + v2.get(i));	
		}
		
		return sums;
	}
	
	private List<Double> divideVector(List<Double> v, double scalar) {
		List<Double> scaledVector = new ArrayList<Double>(v.size());
		
		for (Double component : v) {
			scaledVector.add(component/scalar);
		}
		
		return scaledVector;
	}
}
