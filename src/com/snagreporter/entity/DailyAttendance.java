package com.snagreporter.entity;

import java.io.Serializable;

public class DailyAttendance implements Serializable {
	
	public String ID;
	public String InspectorID;
	public String InspectorName;
	public String AttendanceDate;
	public int NoOfSkilledEmp;
	public int NoOfUnskilledEmp;
	public String CreatedBy;
	public String CreatedDate;
	public String ModifiedBy;
	public String ModifiedDate;
	public String EnteredOnMachineID;
	public String ContractorID;
	public String ContractorName;
	
	public boolean IsSyncedToWeb;
}
