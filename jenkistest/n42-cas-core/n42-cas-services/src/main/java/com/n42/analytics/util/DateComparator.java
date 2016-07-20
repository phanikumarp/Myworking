package com.n42.analytics.util;

import java.util.Comparator;

import n42.model.domain.DomainBase;

public class DateComparator implements Comparator<DomainBase> {
	public int compare(DomainBase o1, DomainBase o2) {
		return o2.getN42LastUpdatedDate().compareTo(o1.getN42LastUpdatedDate());
	}
}
