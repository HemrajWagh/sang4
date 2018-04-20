package com.snagreporter.entity;

import java.io.Serializable;

public class TradeDependency implements Serializable {
	public String TradeID;
	public String ID;
	public String TradeName;
	public String DependencyID;
	public String DependencyTradeName;
	public String DependencyType;
	public String DependencyValueComplete;
	public String CreatedBy;
	public String CreatedDate;
	public String ModifiedBy;
	public String ModifiedDate;
	public String EnteredOnMachineID;
	
	public String StatusForUpload;
	public boolean IsSyncedToWeb;
	
	

}
