package com.watsoncui.ucr.stat;

public class Format {
	private int coverage;
	private float averageQuality;
	private float qualityStdev;
	private int trueSnp;
	private int[] softResultArray;
	
	
	
	public Format(int coverage, float averageQuality) {
		super();
		this.coverage = coverage;
		this.averageQuality = averageQuality;
		this.trueSnp = 0;
		softResultArray = new int[Software.values().length];
		softResultArray[Software.MULTIGEMS.getValue()] = 0;
		softResultArray[Software.FREEBAYES.getValue()] = 0;
		softResultArray[Software.VARSCAN.getValue()] = 0;
		softResultArray[Software.GATK.getValue()] = 0;
	}

	public int getCoverage() {
		return coverage;
	}
	public void setCoverage(int coverage) {
		this.coverage = coverage;
	}
	public float getAverageQuality() {
		return averageQuality;
	}
	public void setAverageQuality(float averageQuality) {
		this.averageQuality = averageQuality;
	}
	public int getTrueSnp() {
		return trueSnp;
	}
	public void setTrueSnp(int trueSnp) {
		this.trueSnp = trueSnp;
	}
	
	public void updateSoftResultArray(int number) {
		this.softResultArray[number] = 1;
	}
	
	public int getSoftSnpResult(int number) {
		return this.softResultArray[number];
	}

	public float getQualityStdev() {
		return qualityStdev;
	}

	public void setQualityStdev(float qualityStdev) {
		this.qualityStdev = qualityStdev;
	}
	
}
