package com.smart.evie;

public class VectorUtil {

	public double cosineSimilarity(double[] v1, double[] v2) {
		return dotProduct(v1, v2)/(magnitude(v1)*magnitude(v2));
	}
	
	private double dotProduct(double[] v1, double[] v2) {
		double sum = 0;
		
		for (int i = 0; i < Math.max(v1.length, v2.length); ++i) {
			sum += v1[i]*v2[i];
		}
		
		return sum;
	}

	private double magnitude(double[] v) {
		double sum = 0;
		
		for (int i = 0; i < v.length; ++i) {
			sum += Math.pow(v[i],2);
		}
		
		return Math.sqrt(sum);
	}

	public double euclideanDistance(double[] v1, double[] v2) {
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
	
	public double[] sumVectors(double[] v1, double[] v2) {
		double[] sums = new double[Math.max(v1.length, v2.length)];
		
		for (int i = 0; i < sums.length; ++i) {
			sums[i] = v1[i] + v2[i];	
		}
		
		return sums;
	}
	
	public double[] divideVector(double[] v, double scalar) {
		for (int vIndex = 0; vIndex < v.length; ++vIndex) {
			v[vIndex] = v[vIndex]/scalar;
		}
		
		return v;
	}

	public double[] multiplyVector(double[] v, double scalar) {
		for (int vIndex = 0; vIndex < v.length; ++vIndex) {
			v[vIndex] = v[vIndex]*scalar;
		}
		
		return v;
	}
}
