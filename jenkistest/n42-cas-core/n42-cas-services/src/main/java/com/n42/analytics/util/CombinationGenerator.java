package com.n42.analytics.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;


public class CombinationGenerator {

	public static void main(String[] args){
		CombinationGenerator generator = new CombinationGenerator();

		Map<String, List<String>> tagsAndValues = generator.getSampleTagsAndValues();
		Map<String, String> selected = generator.getSampleSelected();

		List<List<String>> combinations = generator.generate(selected, tagsAndValues);

		System.out.println("Combination ; " + combinations);		
	}

	private Map<String, List<String>> getSampleTagsAndValues() {
		Map<String, List<String>> tagsAndValues = new HashMap<String, List<String>>();

		List<String> values1 = new ArrayList<String>();
		values1.add("V1"); values1.add("V2"); values1.add("V3");
		tagsAndValues.put("T1", values1);

		List<String> values2 = new ArrayList<String>();
		values2.add("V4"); values2.add("V5");
		tagsAndValues.put("T2", values2);

		List<String> values3 = new ArrayList<String>();
		values3.add("V6"); values3.add("V7"); values3.add("V8");
		tagsAndValues.put("T3", values3);

		return tagsAndValues;
	}

	private Map<String, String> getSampleSelected() {
		Map<String, String>  selectedTags = new HashMap<String, String>();

		selectedTags.put("T1", "V2");
		selectedTags.put("T2", "V4");

		return selectedTags;
	}

	public List<List<String>> generate(Map<String, String> selected, Map<String, List<String>> tagsAndValues) {
		List<List<String>> combinations = new ArrayList<List<String>>();

		Iterator<Entry<String, List<String>>> iterator = tagsAndValues.entrySet().iterator();

		while(iterator.hasNext()){

			Entry<String, List<String>> entry = iterator.next();
			String tagName = entry.getKey();
			List<String> valuesForTag = entry.getValue();

			if(MapUtils.isNotEmpty(selected)) {
				String selectedValueForTag = selected.get(tagName);
				if(selectedValueForTag != null){
					valuesForTag = new ArrayList<String>();
					valuesForTag.add(selectedValueForTag);
				}
			}

			combinations = merge(combinations, valuesForTag);
		}

		return combinations;
	}

	public List<List<String>> merge(List<List<String>> combinations, List<String> list) {
		if(CollectionUtils.isEmpty(combinations)){
			combinations = new ArrayList<>();
			for(String value : list){
				List<String> values = new ArrayList<String>();
				values.add(value);
				combinations.add(values);
			}

			return combinations;
		}

		List<List<String>> newCombinations = new ArrayList<List<String>>();

		for(List<String> combination : combinations){
			for(String value : list){
				List<String> newCombination = new ArrayList<String>(combination);
				newCombination.add(value);
				newCombinations.add(newCombination);
			}
		}		
		return newCombinations;
	}
}
