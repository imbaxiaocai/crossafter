package com.example.crossafter.goods.bean;

public class EvalDetail {
    private int oid;			//订单号
    private int gid;			//商品id
    private int uid;			//零售商id
    private String content;		//评价内容
    private double evaluation;	//评分
    private String uname;		//零售商名

//    public EvalDetail() {
//		// TODO Auto-generated constructor stub
//    	oid = -1;
//    	gid = -1;
//    	uid = -1;
//    	content = null;
//    	evaluation = (Double) null;
//    	uname = null;
//	}
    
    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
