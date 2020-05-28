package com.example.crossafter.recommend.service.impl;

import com.example.crossafter.goods.bean.EvalDetail;
import com.example.crossafter.goods.bean.Evaluation;
import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.goods.dao.GoodMapper;
import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.bean.UserBehavior;
import com.example.crossafter.pub.dao.UserBehaviorMapper;
import com.example.crossafter.recommend.dao.RecommendMapper;
import com.example.crossafter.recommend.service.RecommendService;
import com.example.crossafter.recommend.util.Utils;

import org.apache.tomcat.jni.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.server.UID;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RecommendServiceImpl implements RecommendService{
    @Autowired
    RecommendMapper recommendMapper;
    @Autowired
	private GoodMapper goodMapper;
    @Autowired
    private UserBehaviorMapper userBehaviorMapper;
        
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
	public RespEntity updateRecommend(int uid, int reCounts) throws Exception {
		// TODO Auto-generated method stub
    	RespEntity respEntity = new RespEntity();
    	List<Integer> recommend = new LinkedList<>();
    	//用户区分
    	int count = recommendMapper.getCountByUid(uid);
    	if (count <= 0) {	//新用户
    		recommend.addAll(recommendMapper.getTopEvaluation());
		}else if (count <= Utils.MIN_SCORE) {	//数据稀疏性用户	
			LocalThread_getPIB thread_item = new LocalThread_getPIB(uid);
			LocalThread_getPUB thread_user = new LocalThread_getPUB(uid);
			LocalThread_getTop thread_top = new LocalThread_getTop();
			thread_item.start();
			thread_user.start();
			thread_top.start();
			
			thread_item.join();
			thread_user.join();
			thread_top.join();
			
			List<Integer> temp = thread_top.tops;
			
			recommend = Utils.getRecom(recommend, thread_item.items, temp, reCounts * 0.2);
			recommend = Utils.getRecom(recommend, thread_user.users, temp, reCounts * 0.2);
			for (Integer integer : temp) {
				if (!recommend.contains(integer) && recommend.size() < reCounts) {
					recommend.add(integer);
				}
			}
			
		}else {		//非数据稀疏性用户
			LocalThread_getPIB thread_item = new LocalThread_getPIB(uid);
			LocalThread_getPUB thread_user = new LocalThread_getPUB(uid);
			thread_item.start();
			thread_user.start();
			
			thread_item.join();
			thread_user.join();
			
			recommend.addAll(thread_item.items.subList(0, reCounts / 2));
			recommend.addAll(thread_user.users.subList(0, reCounts / 2));
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
    	//20200515修改返回的信息
		List<Good> results = new ArrayList<Good>();
		for(int i=0;i<recommend.size();i++){
		    Good good = goodMapper.getGoodById(recommend.get(i));
		    if(good.getStatus()==0) {
                results.add(good);
            }
		}
    	//推荐结果写入
    	respEntity.setData(results);
    	respEntity.setHead(RespHead.SUCCESS);
		return respEntity;
	}
    
    /*
     * 用户uid的相似用户列表
     * @param int uid	用户编号
     * @return Map<Integer, Double> key为用户id value为用户之间的相似值
     */
    private Map<Integer, Double> getSim(int uid) {
    	Map<Integer, Double> simUid = new LinkedHashMap<>();	//最终结果数据集
    	
    	LocalThread_getBeByUid thread_behaviors = new LocalThread_getBeByUid(uid);
    	LocalThread_getEvaByUid thread_evals = new LocalThread_getEvaByUid(uid);
    	thread_behaviors.start();
    	thread_evals.start();
    	
    	try {
			thread_behaviors.join();
	    	thread_evals.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	//计算用户对商品数据集    	
    	Map<Integer, Double> X = Utils.getUserGoodMap(thread_evals.evalDetails, thread_behaviors.behaviors);
    	
    	//筛选相似用户
    	Set<Integer> uidSet = new HashSet<>();
    	for (EvalDetail e : thread_evals.evalDetails) {
			List<Integer> uidlist = recommendMapper.getUidByGid(e.getGid());
			uidSet.addAll(uidlist);
		}
    	//计算相似用户的相似度并选出前10位相似用户
    	for (Integer u : uidSet) {
    		LocalThread_getBeByUid thread_behaviors_Y = new LocalThread_getBeByUid(u);
        	LocalThread_getEvaByUid thread_evals_Y = new LocalThread_getEvaByUid(u);
        	thread_behaviors_Y.start();
        	thread_evals_Y.start();
        	
        	try {
            	thread_behaviors_Y.join();
				thread_evals_Y.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
			Map<Integer, Double> Y = Utils.getUserGoodMap(thread_evals_Y.evalDetails, thread_behaviors_Y.behaviors);
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
    		LocalThread_getBeByGid thread_be = new LocalThread_getBeByGid(i);
    		LocalThread_getEvaByGid thread_eva = new LocalThread_getEvaByGid(i);
    		thread_be.start();
    		thread_eva.start();
    		
    		try {
				thread_be.join();
	    		thread_eva.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Map<Integer, Double> map = Utils.getGoodUserMap(thread_eva.evals, thread_be.behaviors);
			
			double p = 0;
			for (Integer u : map.keySet()) {
				p += map.get(u) * simUser.get(u);
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
    	
    	//寻找购买同一商品的用户
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
				if (userList.contains(integer) && 
						recommendMapper.getEvaluation(i, integer).get(0) >= Utils.LIKE_EVALUATION) {
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
    
    
    public class LocalThread_getPIB extends Thread{
    	List<Integer> items;
    	int uid;
    	public LocalThread_getPIB(int uid) {
			// TODO Auto-generated constructor stub
    		this.uid = uid;
		}
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
//    		super.run();
    		items = getP_itemBased(uid);
    	}
    }
    
    public class LocalThread_getPUB extends Thread{
    	List<Integer> users;
    	int uid;
    	public LocalThread_getPUB(int uid) {
			// TODO Auto-generated constructor stub
    		this.uid = uid;
		}
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
//    		super.run();
    		users = getP_userBased(uid);
    	}
    }
    
    public class LocalThread_getTop extends Thread{
    	List<Integer> tops;
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
//    		super.run();
    		tops = recommendMapper.getTopEvaluation();
    	}
    }
    
    public class LocalThread_getEvaByUid extends Thread{
    	List<EvalDetail> evalDetails;
    	int uid;
    	
    	public LocalThread_getEvaByUid(int uid) {
			// TODO Auto-generated constructor stub
    		this.uid = uid;
		}
    	
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
//    		super.run();
    		evalDetails = recommendMapper.getAllEvaluationByUid(uid);
    	}
    }
    
    public class LocalThread_getBeByUid extends Thread{
    	List<UserBehavior> behaviors;
    	int uid;
    	
    	public LocalThread_getBeByUid(int uid) {
			// TODO Auto-generated constructor stub
    		this.uid = uid;
		}
    	
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
//    		super.run();
    		behaviors = userBehaviorMapper.getScoreByUid(uid);
    	}
    }
    
    public class LocalThread_getBeByGid extends Thread{
    	List<UserBehavior> behaviors;
    	int gid;
    	
    	public LocalThread_getBeByGid(int gid) {
			// TODO Auto-generated constructor stub
    		this.gid = gid;
		}
    	
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
//    		super.run();
    		behaviors = userBehaviorMapper.getScoreByGid(gid);
    	}
    }
    
    public class LocalThread_getEvaByGid extends Thread{
    	List<EvalDetail> evals;
    	int gid;
    	public LocalThread_getEvaByGid(int gid) {
			// TODO Auto-generated constructor stub
    		this.gid = gid;
		}
    	
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
//    		super.run();
    		evals = recommendMapper.getAllEvaluationByGid(gid);
    	}
    }
}
