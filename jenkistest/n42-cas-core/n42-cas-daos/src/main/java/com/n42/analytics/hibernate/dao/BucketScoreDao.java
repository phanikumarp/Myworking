package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.BucketScore;

public interface BucketScoreDao {
	
	public BucketScore saveBucketScore(BucketScore bucketScore);

	public List<BucketScore> getAllBucketScore();
	
	public void deleteBucketScore(Integer id);

	public BucketScore getBucketScoreById(Integer id);

}
