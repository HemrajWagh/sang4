package com.snagreporter.entity;

import java.io.Serializable;

public class TradeMaster implements Serializable {
	public String TradeDescription;
	public String ID;
	public String ParentID;
	public String ParentJob;
	public String SeqNo;
	public String ImageName;
	public String CreatedBy;
	public String CreatedDate;
	public String ModifiedBy;
	public String ModifiedDate;
	public String EnteredOnMachineID;
	public String DependencyType;
	public String DependencyValueComplete;
	public String TradeType;
	public int DependencyCount;
	public boolean IsParentCompulsory;
	public String StatusForUpload;
	public boolean IsSyncedToWeb;
	
	

}
