package com.example.crossafter.recommend.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.example.crossafter.goods.bean.EvalDetail;

public class Utils {
	public static final int MIN_SCORE = 10;			//进入榜单需要的最少评价人数
	public static final int MAX_USER_COUNT = 10;	//相似用户的最大选取数目
	public static final int MAX_RECOMMEND = 50;		//单个推荐算法最大推荐商品数目
	public static final int MAX_GOOD_COUNT = 10;	//相似商品的最大选取数目
	public static final double LIKE_EVALUATION = 3;	//用户对喜欢商品的评分
	public static final int RUI = 1;				//简化Rui
	/*
	 * 计算商品权重
	 * @param
	 * double R 普通方法计算的平均分
	 * double C 所有商品的平均分
	 * int v 	商品的评价人数
	 * @return double 商品的权重值
	 */
	public static double getWR(double R, double C, int v) {
		if (v < MIN_SCORE) {
			return -1;		//商品评分人数不足
		}
		double WR = (v / (v + MIN_SCORE)) * R + (MIN_SCORE / (MIN_SCORE + v)) * C;
		return WR;
	}
	
	/*
	 * 计算用户之间的相似度
	 * @param
	 * Map<Integer, Integer> X 用户X对商品的评价数据集
	 * Map<Integer, Integer> Y 用户Y对商品的评价数据集
	 * @return double 用户X与用户Y的相似度
	 */
	public static double getSim(Map<Integer, Double> X, Map<Integer, Double> Y) {
		int dot = 0;	//X与Y数量集的点乘结果
		for (Integer gid : X.keySet()) {
			if (Y.containsKey(gid)) {
				dot += X.get(gid) * Y.get(gid);
			}
		}
		
		double sim = (double)dot / (X.size() * Y.size());
		return sim;
		
	}
	
	//生成用户喜爱商品的数据集
	public static Map<Integer, Double> getGoodData(List<EvalDetail> list){
		Map<Integer, Double> X = new HashMap<Integer, Double>();
		for (EvalDetail e : list) {
    		if (X.containsKey(e.getGid())) {
				X.put(e.getGid(), e.getEvaluation() + X.get(e.getGid()));
			}
			X.put(e.getGid(), e.getEvaluation());
		}
		return X;
	}
	
	//map按值降序排序
	public static Map<Integer, Double> sortByValue(Map<Integer, Double> m){
		if (m == null || m.isEmpty()) {
			return null;
		}
		Map<Integer, Double> sortm = new LinkedHashMap<>();
		List<Entry<Integer, Double>> entryList = new ArrayList<>(m.entrySet());
		Collections.sort(entryList, new Comparator<Entry<Integer, Double>>() {

			@Override
			public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
				// TODO Auto-generated method stub
				return (int) (o2.getValue() - o1.getValue());
			}
			
		});
		Iterator<Entry<Integer, Double>> iterator = entryList.iterator();
		Entry<Integer, Double> temp = null;
		while(iterator.hasNext()) {
			temp = iterator.next();
			sortm.put(temp.getKey(), temp.getValue());
		}
		return sortm;
	}
	
	/*
	 * 计算商品相似度
	 * @param
	 * int Ni 喜欢商品i的用户数
	 * int Nj 喜欢商品j的用户数
	 * int Nij 同时喜欢i和j的用户数
	 * @return double i和j的商品相似度w
	 */
	public static double getW(int Ni, int Nj, int Nij) {
		return (double)Nij / Math.sqrt(Ni * Nj);
	}
	
	//选取推荐商品
	public static List<Integer> getRecom(
			List<Integer> re, List<Integer> src, List<Integer> weight, double count){
		int n = 0;
		for (Integer integer : src) {
			if (weight.contains(integer) && !re.contains(integer)) {
				re.add(integer);
				n ++;
			}
			if (n > count) {
				break;
			}
		}
		return re;
	}
}
