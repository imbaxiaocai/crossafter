package com.example.crossafter.recommend.util;

public class Utils {
	static final int MIN_SCORE = 10;	//进入榜单需要的最少评价人数

	/*
	 * 计算商品权重
	 * @param
	 * double R 普通方法计算的平均分
	 * double C 所有商品的平均分
	 * int v 	商品的评价人数
	 * @return double 商品的权重值
	 */
	public double getWR(double R, double C, int v) {
		if (v < MIN_SCORE) {
			return -1;		//商品评分人数不足
		}
		double WR = (v / (v + MIN_SCORE)) * R + (MIN_SCORE / (MIN_SCORE + v)) * C;
		return WR;
	}
}
