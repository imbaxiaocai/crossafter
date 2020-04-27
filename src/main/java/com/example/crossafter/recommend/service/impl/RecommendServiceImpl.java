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
                double WR = new Utils().getWR(e.getEvaluation(), C, e.getAmount());
                recommendMapper.updateWR(WR, e.getGid());
            }
        }catch(Exception e) {
            e.printStackTrace();
            respEntity.setHead(RespHead.SYS_ERROE);
            return respEntity;
        }
        return respEntity;
    }
    
    /*
     * 用户uid的相似用户列表
     * @param int uid	用户编号
     * @return Map<Integer, Double> key为用户id value为用户之间的相似值
     */
    private Map<Integer, Double> getSim(int uid){
    	Utils utils = new Utils();
    	Map<Integer, Double> simUid = new LinkedHashMap<>();	//最终结果数据集
    	List<EvalDetail> list = recommendMapper.getAllEvaluationByUid(uid);
    	Map<Integer, Double> X = utils.getGoodData(list);
    	//筛选相似用户
    	Set<Integer> uidSet = new HashSet<>();
    	for (EvalDetail e : list) {
			List<Integer> uidlist = recommendMapper.getUidByGid(e.getGid(), e.getEvaluation());
			uidSet.addAll(uidlist);
		}
    	//计算相似用户的相似度并选出前10位相似用户
    	for (Integer u : uidSet) {
			List<EvalDetail> uList = recommendMapper.getAllEvaluationByUid(u);
			Map<Integer, Double> Y = utils.getGoodData(uList);
			double sim = utils.getSim(X, Y);
			
			if (simUid.size() < Utils.MAX_COUNT) {
				simUid.put(u, sim);
				if (simUid.size() == Utils.MAX_COUNT) {
					simUid = utils.sortByValue(simUid);
				}
			}else {
				Map.Entry<Integer, Double> entry = simUid.entrySet().iterator().next();
				if (sim > entry.getValue()) {
					simUid.remove(entry.getKey());
					simUid.put(u, sim);
					simUid = utils.sortByValue(simUid);
				}
			}
		}
    	return simUid;
    }
    
    //用户X的候选商品
    private Set<Integer> getCandidate(int uid){
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
    private List<Integer> getP(int uid){
    	List<Integer> result = new LinkedList<Integer>();
    	
    	Set<Integer> candidate = getCandidate(uid);
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
}
