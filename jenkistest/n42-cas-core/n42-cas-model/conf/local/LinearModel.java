package com.inferneon.supervised.linearregression;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.inferneon.core.Attribute;
import com.inferneon.core.IInstances;
import com.inferneon.core.IInstances.Context;
import com.inferneon.core.Instance;
import com.inferneon.core.Instances;
import com.inferneon.core.Value;
import com.inferneon.core.exceptions.IncompatibleMatrixOperation;
import com.inferneon.core.exceptions.MatrixElementIndexOutOfBounds;
import com.inferneon.core.matrices.IMatrix;
import com.inferneon.supervised.linearregression.LinearRegression.Method;

public class LinearModel {

	private IInstances trainingInstances;
	private Method method;
	private long numInstances;

	// The possible models
	private double averageClassValue;
	private IMatrix parametersMatrix;
	private double[] sgdParams;	

	public long getNumInstances() {
		return numInstances;
	}

	public double getAveClassalueOfInstances() {
		return averageClassValue;
	}

	public LinearModel(long numInstances, double aveClassalueOfInstances){
		this.numInstances = numInstances;
		this.averageClassValue = aveClassalueOfInstances;
	}

	public LinearModel(IInstances trainingInstances, Method method, IMatrix parametersMatrix, double[] sgdParams) {
		this.trainingInstances = trainingInstances;
		this.method = method;
		this.parametersMatrix = parametersMatrix;
		this.sgdParams = sgdParams;

	}

	public double[] getSgdRegressionParams(){
		return sgdParams;
	}

	public IMatrix getParametersMatrix() {
		return parametersMatrix;
	}

	public void setParametersMatrix(IMatrix parametersMatrix) {
		this.parametersMatrix = parametersMatrix;
	}

	public Value classify(Instance instance) {
		if(parametersMatrix == null && sgdParams == null){
			// Could not create a linear model for this during training; lets return the average value
			// of the class attribute			
			return new Value(averageClassValue);			
		}

		List<Instance> testInstsList = new ArrayList<>();

		testInstsList.add(instance);
		Instances testInstances = new Instances(trainingInstances.context, testInstsList, 
				trainingInstances.getAttributes(), trainingInstances.getClassIndex());
		try {

			if(method == Method.SIMPLE_LINEAR_REGRESSION){
				IMatrix XTest = testInstances.matrixAndClassVector(false)[0];
				IMatrix product = XTest.product(parametersMatrix);
				return new Value(product.getElement(0, 0));
			}
			else if(method == Method.RIDGE  || method == Method.FORWARD_STAGEWISE_REGRESSION){
				IMatrix XTest = testInstances.matrixAndClassVector(false)[0];
				IMatrix product = XTest.product(parametersMatrix);
				IMatrix[] XAndY = trainingInstances.matrixAndClassVector(false);
				IMatrix XMeansTraining = XAndY[0].means(true);
				IMatrix XVarTraining = XAndY[0].variances(XMeansTraining, true);

				XMeansTraining.product(-1.0);

				IMatrix numeratorMat = XTest.add(XMeansTraining);	

				IMatrix normalizedMat = numeratorMat.divide(true, XVarTraining);			    

				IMatrix yEst = normalizedMat.product(parametersMatrix);
				IMatrix YMeansTraining = XAndY[1].means(true);
				yEst = yEst.add(YMeansTraining);

				double predicted = yEst.getElement(0, 0);
				return new Value(predicted);
			}
			else if(method == Method.GRADIENT_DESCENT ){				
				return new Value(instance.dotProd(instance.getValues().size(), sgdParams, instance.getValues().size()-1));				
			}
		} catch (IncompatibleMatrixOperation | MatrixElementIndexOutOfBounds e) {
			Assert.assertTrue(false);
		}
		return null;
	}

	public int getNumLinearModelParameters() {

		if(parametersMatrix == null && sgdParams == null){
			return 1;
		}

		if(method == Method.SIMPLE_LINEAR_REGRESSION || method == Method.RIDGE  || method == Method.FORWARD_STAGEWISE_REGRESSION){
			return (int) parametersMatrix.getNumRows();
		}
		else if(method == Method.GRADIENT_DESCENT){
			return sgdParams.length;
		}
		return 0;
	}	

	@Override
	public String toString(){
		String description = "LM:";
		description += "(" + numInstances + " / " + averageClassValue + "): ";

		if(parametersMatrix == null && sgdParams == null){
			return averageClassValue + "";
		}

		try{
			List<Attribute> attributes = trainingInstances.getAttributes();
			int numAttrs = attributes.size() -1;

			if(method == Method.SIMPLE_LINEAR_REGRESSION || method == Method.RIDGE  || method == Method.FORWARD_STAGEWISE_REGRESSION){
				description += parametersMatrix.getElement(0, 0);
				for(int i = 1; i < numAttrs; i++){
					double element = parametersMatrix.getElement(i, 0);
					if(Double.compare(element, 0.0) >= 0){
						description += " + ";
					}
					description += element + "*" + attributes.get(i).getName();
				}
			}
			else if(method == Method.GRADIENT_DESCENT){
				description += sgdParams[0];
				for(int i = 1; i < numAttrs; i++){
					double element = sgdParams[i];
					if(Double.compare(element, 0.0) >= 0){
						description += " + ";
					}
					description += sgdParams[i] + " * " + attributes.get(i).getName();
				}
			}
		}
		catch(Exception e){
			description += " <Error building linear model description>";
		}

		return description;
	}
}
