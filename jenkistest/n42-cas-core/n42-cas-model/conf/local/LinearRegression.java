package com.inferneon.supervised.linearregression;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

import com.inferneon.core.Attribute;
import com.inferneon.core.IInstances;
import com.inferneon.core.Instance;
import com.inferneon.core.Instances;
import com.inferneon.core.Value;
import com.inferneon.core.exceptions.IllegalOperationOnSigularMatrix;
import com.inferneon.core.exceptions.IncompatibleMatrixOperation;
import com.inferneon.core.exceptions.MatrixElementIndexOutOfBounds;
import com.inferneon.core.matrices.IMatrix;
import com.inferneon.core.matrices.MatrixElementOperation;
import com.inferneon.supervised.Supervised;

public class LinearRegression extends Supervised {

	private static final Logger LOG = LoggerFactory.getLogger(LinearRegression.class);
	public enum Method {
		SIMPLE_LINEAR_REGRESSION,
		RIDGE,
		FORWARD_STAGEWISE_REGRESSION,
		GRADIENT_DESCENT,
		STOCHASTIC_GRADIENT_DESCENT
	}

	private IInstances trainingInstances;
	
	// The method used
	private Method method;	
	
	// The possible models
	private IMatrix parameters;
	private double[] sgdParams;
	private double averageValue;

	private Double ridgeConstant;
	private Double stepSize;
	private int numIterations;
	private Boolean isStochastic = false;
	
	// Final linear model created after training.
	private LinearModel linearModel;
	
	public LinearRegression(){
		method = Method.SIMPLE_LINEAR_REGRESSION;
	}

	public LinearRegression(Double ridgeConstant){
		method = Method.RIDGE;
		this.ridgeConstant = ridgeConstant;
	}

	public LinearRegression(double stepSize, int numIterations){
		method = Method.FORWARD_STAGEWISE_REGRESSION;
		this.stepSize = stepSize;
		this.numIterations = numIterations;
	}

	public LinearRegression(double stepSize, int numIterations, boolean isStochastic){
		this.isStochastic = isStochastic;
		if(isStochastic == true){
			method = Method.STOCHASTIC_GRADIENT_DESCENT;
		}else {
			method = Method.GRADIENT_DESCENT;
		}
		this.stepSize = stepSize;
		this.numIterations = numIterations;
	}
	
	@Override
	public void train(IInstances instances) throws Exception {
		this.trainingInstances = instances;
		
		List<Attribute> attributes = instances.getAttributes();
		if(attributes.size() == 1){
			// Only class is available, compute the average value
			averageValue = instances.mean(attributes.get(0), 0L, instances.size());
			
			// Initialize final linear model that was determined			
			linearModel = new LinearModel(trainingInstances.size(), averageValue);
			return;
		}
		
		if(method == Method.SIMPLE_LINEAR_REGRESSION || method == Method.RIDGE){
			normalRegression(instances);		
		}		
		else if(method == Method.FORWARD_STAGEWISE_REGRESSION){
			forwardStagewiseRegression(instances);
		}
		else if(method == Method.GRADIENT_DESCENT){
			gradientDescent(instances);
		}
		else if(method == Method.STOCHASTIC_GRADIENT_DESCENT){
			stochasticGradientDescent(instances);
		}
		else{
			// Default to simple linear regression using normal equation
			normalRegression(instances);
		}
		
		// Initialize final linear model that was determined
		linearModel = new LinearModel(trainingInstances, method, parameters, sgdParams);
	}

	private void normalRegression(IInstances instances)throws IllegalOperationOnSigularMatrix, 
	IncompatibleMatrixOperation, MatrixElementIndexOutOfBounds {
		IMatrix[] XAndY = null;
		if(method == Method.RIDGE){
			XAndY = instances.matrixAndClassVector(true);
		}
		else{
			XAndY = instances.matrixAndClassVector(false);
		}
		IMatrix X = XAndY[0];
		IMatrix Y = XAndY[1];		
		IMatrix XT = X.transpose();		
		IMatrix XTX = XT.product(X);

		if(method == Method.RIDGE){
			// The data should be normalized.
			IMatrix identity = XTX.identity();
			identity.product(ridgeConstant);
			XTX = XTX.add(identity);
		}

		IMatrix XTXI = XTX.inverse();
		if(XTXI != null){
			// Singular matrix?
			parameters = XTXI.product(XT.product(Y));	
		}
	}

	private void forwardStagewiseRegression(IInstances instances) {
		IMatrix[] XAndY = instances.matrixAndClassVector(true);

		long numColumns = (int) XAndY[0].getNumColumns();
		parameters = XAndY[0].initialize(0.0, false, true);
			
		IMatrix maxParams = parameters.copy();
		
		try{
			for(int i = 0; i < numIterations; i++){
				double lowestError = Double.MAX_VALUE;
				LOG.debug("Params: {}" + parameters);
				for(int j = 0; j < numColumns; j++){

					// Increase the weight
					double factor = stepSize;
					IMatrix currParameters1 = parameters.copy();
					currParameters1.setElement(0, j, factor, MatrixElementOperation.ADD);
					IMatrix yHatMat = XAndY[0].product(currParameters1.transpose());				
					double errorOnIncrement = rootMeanSquareError(XAndY[1], yHatMat);

					// Decrease the weight
					IMatrix currParameters2 = parameters.copy();
					currParameters2.setElement(0, j, -1.0 * stepSize , MatrixElementOperation.ADD);
					yHatMat = XAndY[0].product(currParameters2.transpose());	
					double errorOnDecrement = rootMeanSquareError(XAndY[1], yHatMat);

					// Pick the lesser of the two errors
					IMatrix paramsWithLesserError = currParameters1;
					double currentError = errorOnIncrement;
					if(Double.compare(currentError, errorOnDecrement) > 0){
						currentError = errorOnDecrement;
						paramsWithLesserError = currParameters2;
					}
										
					if(Double.compare(currentError, lowestError) <  0){
						lowestError = currentError;
						maxParams = paramsWithLesserError;
					}
				}				
				parameters = maxParams.copy();
			}
		}
		catch(Exception e){
			// TODO Should not happen. Log here

		}
		
		parameters = parameters.transpose();
	}

	private double rootMeanSquareError(IMatrix yTrainMat, IMatrix yHatMat) 
			throws IncompatibleMatrixOperation, MatrixElementIndexOutOfBounds{
		yHatMat.product(-1.0);
		IMatrix diff = yTrainMat.add(yHatMat);
		
		int numRows = (int) yTrainMat.getNumRows();
		double sum = 0.0;
		for(int m = 0; m < numRows; m++){
			sum += Math.pow(diff.getElement(m, 0), 2.0);
		}
		
		return sum;
	}	

	public LinearModel getLinearModel(){
		return linearModel;
	}
	
	@Override
	public Value classify(Instance instance) {
		return linearModel.classify(instance);
	}

	public IMatrix getParameters(){
		return parameters;
	}
	
	private void gradientDescent(IInstances instances) {
		this.trainingInstances = instances;
		sgdParams = new double[instances.getAttributes().size()-1];
		sgdParams = trainingInstances.gradientDescentForLinearRegression(sgdParams, numIterations, stepSize);
	}

	private void stochasticGradientDescent(IInstances instances) {
		this.trainingInstances = instances;
		sgdParams = new double[instances.getAttributes().size()-1];
		sgdParams = trainingInstances.stochasticGradientDescentForLinearRegression(sgdParams, numIterations, stepSize);
	}
	
	public double[] getLinearRegressionParams(){
		return sgdParams;
	}
	
	public int getNumLinearModelParameters(){
		if(parameters == null){
			return 1;
		}
		
		return (int)parameters.getNumRows();
	}	
}
