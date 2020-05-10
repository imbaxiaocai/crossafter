package com.example.crossafter.recommend.service.impl;

import com.example.crossafter.goods.bean.EvalDetail;
import com.example.crossafter.goods.bean.Evaluation;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.recommend.dao.RecommendMapper;
import com.example.crossafter.recommend.service.RecommendService;
import com.example.crossafter.recommend.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RecommendServiceImpl implements RecommendService{
    @Autowired
    RecommendMapper recommendMapper;
    //计算商品的权重
    @Override
    public RespEntity updateWR() {
        // TODO Auto-generated method stub
        RespEntity respEntity = new RespEntity();
        try {
            List<Evaluation> list = recommendMapper.getAll();
            double sum = 0;
            for (Evaluation e : list) {
                sum += e.getEvaluation();
            }
            double C = sum / list.size();

            for (Evaluation e : list) {
                new Utils();
				double WR = Utils.getWR(e.getEvaluation(), C, e.getAmount());
                recommendMapper.updateWR(WR, e.getGid());
            }
        }catch(Exception e) {
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    
    @Override
	public RespEntity updateRecommend(int uid, int reCounts) {
		// TODO Auto-generated method stub
    	RespEntity respEntity = new RespEntity();
    	List<Integer> recommend = new LinkedList<>();
    	//用户区分
    	int count = recommendMapper.getCountByUid(uid);
    	if (count <= 0) {	//新用户
    		recommend.addAll(recommendMapper.getTopEvaluation());
		}else if (count <= Utils.MIN_SCORE) {	//数据稀疏性用户
			List<Integer> item_temp = getP_itemBased(uid);
			List<Integer> user_temp = getP_userBased(uid);
			List<Integer> temp = recommendMapper.getTopEvaluation();
			
			recommend = Utils.getRecom(recommend, item_temp, temp, reCounts * 0.2);
			recommend = Utils.getRecom(recommend, user_temp, temp, reCounts * 0.2);
			for (Integer integer : temp) {
				
				if (!recommend.contains(integer) && recommend.size() < reCounts) {
					recommend.add(integer);
				}
			}
			
		}else {		//非数据稀疏性用户
			List<Integer> item_temp = getP_itemBased(uid);
			List<Integer> user_temp = getP_userBased(uid);
			
			recommend.addAll(item_temp.subList(0, reCounts / 2));
			recommend.addAll(user_temp.subList(0, reCounts / 2));
		}
    	
    	//对推荐结果按照权重排序
    	recommend.sort(new Comparator<Integer>() {
    		@Override
    		public int compare(Integer o1, Integer o2) {
    			// TODO Auto-generated method stub
    			int o1_weight = recommendMapper.getweightByGid(o1);
    			int o2_weight = recommendMapper.getweightByGid(o2);
    			return o2_weight - o1_weight;	//降序
    		}
		});
    	
    	//推荐结果写入
    	respEntity.setData(recommend);
    	respEntity.setHead(RespHead.SUCCESS);
		return respEntity;
	}
    
    /*
     * 用户uid的相似用户列表
     * @param int uid	用户编号
     * @return Map<Integer, Double> key为用户id value为用户之间的相似值
     */
    private Map<Integer, Double> getSim(int uid){
//    	Utils utils = new Utils();
    	Map<Integer, Double> simUid = new LinkedHashMap<>();	//最终结果数据集
    	List<EvalDetail> list = recommendMapper.getAllEvaluationByUid(uid);
    	Map<Integer, Double> X = Utils.getGoodData(list);
    	//筛选相似用户
    	Set<Integer> uidSet = new HashSet<>();
    	for (EvalDetail e : list) {
			List<Integer> uidlist = recommendMapper.getUidByGid(e.getGid());
			uidSet.addAll(uidlist);
		}
    	//计算相似用户的相似度并选出前10位相似用户
    	for (Integer u : uidSet) {
			List<EvalDetail> uList = recommendMapper.getAllEvaluationByUid(u);
			Map<Integer, Double> Y = Utils.getGoodData(uList);
			double sim = Utils.getSim(X, Y);
			
			if (simUid.size() < Utils.MAX_USER_COUNT) {
				simUid.put(u, sim);
				if (simUid.size() == Utils.MAX_USER_COUNT) {
					simUid = Utils.sortByValue(simUid);
				}
			}else {
				Map.Entry<Integer, Double> entry = simUid.entrySet().iterator().next();
				if (sim > entry.getValue()) {
					simUid.remove(entry.getKey());
					simUid.put(u, sim);
					simUid = Utils.sortByValue(simUid);
				}
			}
		}
    	return simUid;
    }
    
    //用户X的候选商品
    private Set<Integer> getCandidate_userBased(int uid){
    	Set<Integer> candidate = new HashSet<>();
    	List<EvalDetail> uList = recommendMapper.getAllEvaluationByUid(uid);
    	Set<Integer> uLike = new HashSet<>();	//用户u已经有评分的商品号
    	
    	for (EvalDetail e : uList) {
			uLike.add(e.getGid());
		}
    	
    	Map<Integer, Double> simUser = getSim(uid);
    	for (int i : simUser.keySet()) {
			List<EvalDetail> temp = recommendMapper.getAllEvaluationByUid(i);
			
			for (EvalDetail e : temp) {
				if (!uLike.contains(e.getGid())) {
					candidate.add(e.getGid());
				}
			}
		}
    	return candidate;
    }
    
    //用户对候选商品的感兴趣程度
    private List<Integer> getP_userBased(int uid){
    	List<Integer> result = new LinkedList<Integer>();
    	
    	Set<Integer> candidate = getCandidate_userBased(uid);
    	Map<Integer, Double> simUser = getSim(uid);
    	for (Integer i : candidate) {
			List<EvalDetail> list = recommendMapper.getAllEvaluationByGid(i);
			double p = 0;
			for (EvalDetail e : list) {
				p += e.getEvaluation() * simUser.get(e.getUid());
			}
			
			int j = 0;
			for(;j < result.size(); j ++) {
				if (p > result.get(j)) {
					break;
				}
			}
			result.add(j, i);
			if (result.size() > Utils.MAX_RECOMMEND) {
				result.remove(Utils.MAX_RECOMMEND);
			}
		}
    	return result;
    }
    
    /*
     * 商品gid的相似商品列表
     * @param int gid	商品编号
     * @return Map<Integer, Double> key为商品id value为商品之间的相似值
     */
    private Map<Integer, Double> getW(int gid){
    	Map<Integer, Double> result = new LinkedHashMap<>();
    	
    	//寻找购买统一商品的用户
    	List<Integer> userList = recommendMapper.getUidByGid(gid);
    	//筛选商品
    	Set<Integer> gids = new HashSet<>();
    	for (int i : userList) {
			List<EvalDetail> gList = recommendMapper.getAllEvaluationByUid(i);
			
			for (EvalDetail evalDetail : gList) {
				gids.add(evalDetail.getGid());
			}
		}
    	//计算相似商品的相似度并选取前10位
    	for (Integer i : gids) {
			List<Integer> uList = recommendMapper.getUidByGid(i);
			
			//同时喜欢两种商品的人数
			int count = 0;
			for (Integer integer : uList) {
				if (userList.contains(integer)) {
					count ++;
				}
			}
			
			double w = Utils.getW(userList.size(), uList.size(), count);
			
			if (result.size() < Utils.MAX_GOOD_COUNT) {
				result.put(i, w);
				if (result.size() == Utils.MAX_GOOD_COUNT) {
					result = Utils.sortByValue(result);
				}
			}else {
				Map.Entry<Integer, Double> entry = result.entrySet().iterator().next();
				if (w > entry.getValue()) {
					result.remove(entry.getKey());
					result.put(i, w);
					result = Utils.sortByValue(result);
				}
			}
		}
    	return result;
    }
    
    //候选商品
    private Set<Integer> getCandidate_itemBased(int uid){
    	Set<Integer> candidates = new HashSet<>();
    	List<Integer> likeGids = recommendMapper.getGidByUid(uid, Utils.LIKE_EVALUATION);	//用户uid喜欢的商品
    	
    	for (Integer i : likeGids) {
			Map<Integer, Double> cand = getW(i);
			for (int j : cand.keySet()) {
				
				if (!(likeGids.contains(j))) {
					candidates.add(j);
				}
			}
		}
    	
    	return candidates;
    }
    //用户对商品的感兴趣程度
    private List<Integer> getP_itemBased(int uid){
    	List<Integer> result = new LinkedList<>();
    	
    	List<Integer> likeGids = recommendMapper.getGidByUid(uid, Utils.LIKE_EVALUATION);	//用户uid喜欢的商品
    	Set<Integer> candidates = getCandidate_itemBased(uid);
    	for (int j : candidates) {				//j为目标商品
			Map<Integer, Double> w = getW(j);	//j的相似商品列表
			
			double p = 0;
			for (Integer i : likeGids) {
				if (w.containsKey(i)) {
					p += w.get(i) * Utils.RUI;
				}
			}
			
			int k = 0;
			for(;k < result.size(); k ++) {
				if (p > result.get(k)) {
					break;
				}
			}
			result.add(k, j);
			if (result.size() > Utils.MAX_RECOMMEND) {
				result.remove(Utils.MAX_RECOMMEND);
			}
		}
    	
    	return result;
    }

	
}
