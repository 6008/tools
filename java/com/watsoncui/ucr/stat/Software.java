package com.watsoncui.ucr.stat;

public enum Software {
	MULTIGEMS(0), FREEBAYES(1), VARSCAN(2), GATK(3), SAMTOOLS(4);
	
	private final int soft;
	Software(int soft) {
		this.soft = soft;
	}
	public int getValue() {
		return soft;
	}
}
