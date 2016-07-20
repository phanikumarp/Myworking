package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.BucketScore;
import n42.domain.model.MetricScore;

public class BucketScoreDaoImpl extends AbstractDao implements BucketScoreDao{

	@Override
	public BucketScore saveBucketScore(BucketScore bucketScore) {
		bucketScore = (BucketScore) setMetaAttributes(bucketScore);
		return (BucketScore) super.save(bucketScore);
	}
	
	@Override
	public List<BucketScore> getAllBucketScore(){
		@SuppressWarnings("unchecked")
		List<BucketScore> BucketScoreObj = (List<BucketScore>) super.findAll(BucketScore.class);
		return BucketScoreObj;
		
	}
	
	@Override
	public void deleteBucketScore(Integer id) {
		super.hardDelete(BucketScore.class, id);
	}
	
	@Override
	public BucketScore getBucketScoreById(Integer id) {
		return (BucketScore) super.findById(BucketScore.class, id);
	}
}
