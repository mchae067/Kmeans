/**
 * A k-means clustering algorithm implementation.
 * 
 */

import java.util.ArrayList;

public class KMeans {
    
	//Method to calculate Euclidean distance
    public Double Euclidean(double[] a, double[] b){
    	if(a.length!=b.length){
    		return null;
    	}
    	double count = 0;
    	for (int i=0; i<a.length; i++) {
    		count += Math.pow((a[i]-b[i]), 2);
		}
		return Double.valueOf(Math.sqrt(count));
    }
    
    //Performs KMeans clustering on data instances[][]
	public KMeansResult cluster(double[][] centroids, double[][] instances, double threshold) {

		int dimensions = centroids[0].length;
    	int[] clusterAssignment = new int[instances.length];
    	ArrayList<Double> distortionIterations = new ArrayList<Double>();
    	
    	while(true){
    		//Determine centroids for clustering
    		for (int i=0; i<instances.length; i++) {
    			double bestDistance = -1;
    			int bestCentroid = -1;
    			for (int j=0; j<centroids.length; j++) {
    				Double distance = Euclidean(centroids[j], instances[i]);
    				if(distance == null){
    					System.out.println("ERROR: Null data");
    					return null;
    				} else if(bestDistance<0 || distance<=bestDistance){
    					bestDistance = distance;
    					bestCentroid = j;
    				}
    			}
    			if(bestCentroid == -1){
    				System.out.println("ERROR: Cannot locate centroid");
    				return null;
    			}
    			clusterAssignment[i] = bestCentroid;
    		}
    		
    		//Determine maximum distance of orphan data
    		boolean[] hasChildren = new boolean[centroids.length];
    		for (int i=0; i<clusterAssignment.length; i++) {
    			hasChildren[clusterAssignment[i]] = true;
			}
    		for (int i=0; i<hasChildren.length; i++) {
    			if(hasChildren[i] == false){
    				double bestDistance = -1;
    				int bestInstance = -1;
    				for (int j=0; j<instances.length; j++) {
    					Double distance = Euclidean(instances[j], centroids[clusterAssignment[j]]);
    					if(distance == null){
    						System.out.println("ERROR: Null data");
        					return null;
    					} else if(bestDistance<0 || distance>bestDistance){
    						bestDistance = distance;
    						bestInstance = j;
    					}
    				}
    				if(bestInstance == -1){
    					System.out.println("ERROR: Cannot find max distance");
    					return null;
    				}
    				clusterAssignment[bestInstance] = i;
    			}
			}
    		
    			
    		//Determine next set of centroids
    		for (int i=0; i<centroids.length; i++) {
    			double[] totalSums = new double[dimensions];
    			int numElements = 0;
    			for (int j=0; j<clusterAssignment.length; j++) {
    				if(clusterAssignment[j] == i){
    					for (int k=0; k<instances[j].length; k++) {
    						totalSums[k] += instances[j][k];
    					}
    					numElements++;
    				}
    			}
    			//Set new centroid
    			for (int l=0; l<totalSums.length; l++) {
    				centroids[i][l] = totalSums[l]/numElements;
    			}
    		}

    		//Calculate distortion
    		double distortion = 0;
    		for (int i=0; i<instances.length; i++) {
    			distortion += Math.pow(Euclidean(instances[i], centroids[clusterAssignment[i]]), 2);
    		}
    		distortionIterations.add(distortion);
    		if(distortionIterations.size()>1 && 
    				distortionIterations.get(distortionIterations.size()-2)-distortion < threshold){
    			break;
    		} else {
    			continue;
    		}
    	}
    	
    	//Return KMeansResult
    	KMeansResult out = new KMeansResult();
    	out.centroids = centroids;
    	out.clusterAssignment = clusterAssignment;
    	out.distortionIterations = new double[distortionIterations.size()];
    	for (int i=0; i<out.distortionIterations.length; i++) {
    		out.distortionIterations[i] = distortionIterations.get(i);
		}
        return out;
    }

}

