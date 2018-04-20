package com.snagreporter.entity;

import java.io.Serializable;

public class SnagMaster implements Serializable {
	String AptAreaID;
	String ID;
	String SnagType;
	String SnagDetails;
	String PictureURL1;
	String PictureURL2;
	String PictureURL3;
	String ProjectID;
	String ProjectName;
	String BuildingID;
	String BuildingName;
	String FloorID;
	String Floor;
	String ApartmentID;
	String Apartment;
	String AptAreaName;
	String ReportDate;
	String SnagStatus;
	String ResolveDate;
	String ExpectedInspectionDate;
	String FaultType;
	String CostTo;
	String SnagPriority;
	Double Cost;
	String InspectorID;
	String InspectorName;
	String ResolveDatePictureURL1;
	String ResolveDatePictureURL2;
	String ResolveDatePictureURL3;
	String ReInspectedUnresolvedDate;
	String ReInspectedUnresolvedDatePictureURL1;
	String ReInspectedUnresolvedDatePictureURL2;
	String ReInspectedUnresolvedDatePictureURL3;
	boolean IsDataSyncToWeb;
	String StatusForUpload;
	String QCC;
	String QccRemarks;
	String AllocatedTo;
	String AllocatedToName;
	String ContractorStatus;
	String ContractorRemarks;
	String ContractorExpectedBeginDate;
	String ContractorExpectedEndDate;
	String ContractorActualEndDate;
	String ContractorActualBeginDate;
	double PercentageCompleted;
	int ConNoOfResources;
	Double ConManHours;
	Double ConCost;
	
	float XValue;
	float YValue;
	
	
	String ContractorID;
	String ContractorName;
	String SubContractorID;
	String SubContractorName;
	String SubSubContractorID;
	String SubSubContractorName;
	
	public void setContractorID(String str)
	{
		ContractorID=str;
	}
	public String getContractorID()
	{
		return ContractorID;
	}
	public void setSubContractorID(String str)
	{
		SubContractorID=str;
	}
	public String getSubContractorID()
	{
		return SubContractorID;
	}
	
	public void setSubSubContractorID(String str)
	{
		SubSubContractorID=str;
	}
	public String getSubSubContractorID()
	{
		return SubSubContractorID;
	}
	public void setSubSubContractorName(String str)
	{
		SubSubContractorName=str;
	}
	public String getSubSubContractorName()
	{
		return SubSubContractorName;
	}
	
	public void setContractorName(String str)
	{
		ContractorName=str;
	}
	public String getContractorName()
	{
		return ContractorName;
	}
	
	public void setSubContractorName(String str)
	{
		SubContractorName=str;
	}
	public String getSubContractorName()
	{
		return SubContractorName;
	}
	
	public void setPercentageCompleted(double id){
		PercentageCompleted=id;
	}
	public double getPercentageCompleted(){
		return PercentageCompleted;
	}
	
	public void setXValue(float id){
		XValue=id;
	}
	public float getXValue(){
		return XValue;
	}
	
	public void setYValue(float id){
		YValue=id;
	}
	public float getYValue(){
		return YValue;
	}
	
	public void setConNoOfResources(int id){
		ConNoOfResources=id;
	}
	public int getConNoOfResources(){
		return ConNoOfResources;
	}
	public void setConManHours(Double id){
		ConManHours=id;
	}
	public Double getConManHours(){
		return ConManHours;
	}
	public void setConCost(Double id){
		ConCost=id;
	}
	public Double getConCost(){
		return ConCost;
	}
	
	public void setContractorStatus(String id){
		ContractorStatus=id;
	}
	public String getContractorStatus(){
		return ContractorStatus;
	}
	
	public void setContractorRemarks(String id){
		ContractorRemarks=id;
	}
	public String getContractorRemarks(){
		return ContractorRemarks;
	}
	
	public void setContractorExpectedBeginDate(String id){
		ContractorExpectedBeginDate=id;
	}
	public String getContractorExpectedBeginDate(){
		return ContractorExpectedBeginDate;
	}
	
	public void setContractorExpectedEndDate(String id){
		ContractorExpectedEndDate=id;
	}
	public String getContractorExpectedEndDate(){
		return ContractorExpectedEndDate;
	}
	public void setContractorActualEndDate(String id){
		ContractorActualEndDate=id;
	}
	public String getContractorActualEndDate(){
		return ContractorActualEndDate;
	}
	
	public void setContractorActualBeginDate(String id){
		ContractorActualBeginDate=id;
	}
	public String getContractorActualBeginDate(){
		return ContractorActualBeginDate;
	}
	
	public void setAllocatedTo(String id){
		AllocatedTo=id;
	}
	public String getAllocatedTo(){
		return AllocatedTo;
	}
	
	public void setAllocatedToName(String id){
		AllocatedToName=id;
	}
	public String getAllocatedToName(){
		return AllocatedToName;
	}
	
	public void setQCC(String id){
		QCC=id;
	}
	public String getQCC(){
		return QCC;
	}
	public void setQccRemarks(String id){
		QccRemarks=id;
	}
	public String getQccRemarks(){
		return QccRemarks;
	}
	
	public void setIsDataSyncToWeb(boolean id){
		IsDataSyncToWeb=id;
	}
	public boolean getIsDataSyncToWeb(){
		return IsDataSyncToWeb;
	}
	
	public void setStatusForUpload(String id){
		StatusForUpload=id;
	}
	public String getStatusForUpload(){
		return StatusForUpload;
	}
	
	public void setExpectedInspectionDate(String id){
		ExpectedInspectionDate=id;
	}
	public String getExpectedInspectionDate(){
		return ExpectedInspectionDate;
	}
	
	public void setFaultType(String id){
		FaultType=id;
	}
	public String getFaultType(){
		return FaultType;
	}
	public void setCost(Double dbl)
	{
		Cost=dbl;
		
	}
	public Double getCost()
	{
		return Cost;
	}
	public void setCostTo(String costto)
	{
		CostTo=costto;
	}
	public String getCostTo()
	{
		return CostTo;
	}
	public void setSnagPriority(String str)
	{
		SnagPriority=str;
	}
	public String getSnagPriority()
	{
		return SnagPriority;
	}
	public void setID(String id){
		ID=id;
	}
	public String getID(){
		return ID;
	}
	
	public void setSnagType(String str){
		SnagType=str;
	}
	public String getSnagType(){
		return SnagType;
	}
	
	public void setSnagDetails(String str){
		SnagDetails=str;
	}
	public String getSnagDetails(){
		return SnagDetails;
	}
	

	public void setPictureURL1(String str){
		PictureURL1=str;
	}
	public String getPictureURL1(){
		return PictureURL1;
	}
	
	public void setPictureURL2(String str){
		PictureURL2=str;
	}
	public String getPictureURL2(){
		return PictureURL2;
	}
	
	public void setPictureURL3(String str){
		PictureURL3=str;
	}
	public String getPictureURL3(){
		return PictureURL3;
	}

	public void setProjectID(String str){
		ProjectID=str;
	}
	public String getProjectID(){
		return ProjectID;
	}
	
	public void setProjectName(String str){
		ProjectName=str;
	}
	public String getProjectName(){
		return ProjectName;
	}
	
	
	public void setBuildingID(String str){
		BuildingID=str;
	}
	public String getBuildingID(){
		return BuildingID;
	}
	
	public void setBuildingName(String str){
		BuildingName=str;
	}
	public String getBuildingName(){
		return BuildingName;
	}
	
	public void setFloorID(String str){
		FloorID=str;
	}
	public String getFloorID(){
		return FloorID;
	}
	
	public void setFloor(String str){
		Floor=str;
	}
	public String getFloor(){
		return Floor;
	}
	
	public void setApartmentID(String str){
		ApartmentID=str;
	}
	public String getApartmentID(){
		return ApartmentID;
	}
	
	public void setApartment(String str){
		Apartment=str;
	}
	public String getApartment(){
		return Apartment;
	}
	
	public void setAptAreaName(String str){
		AptAreaName=str;
	}
	public String getAptAreaName(){
		return AptAreaName;
	}
	
	public void setAptAreaID(String str){
		AptAreaID=str;
	}
	public String getAptAreaID(){
		return AptAreaID;
	}
	
	public void setReportDate(String str){
		ReportDate=str;
	}
	public String getReportDate(){
		return ReportDate;
	}
	
	public void setSnagStatus(String str){
		SnagStatus=str;
	}
	public String getSnagStatus(){
		return SnagStatus;
	}
	
	public void setResolveDate(String str){
		ResolveDate=str;
	}
	public String getResolveDate(){
		return ResolveDate;
	}
	
	public void setInspectorID(String str){
		InspectorID=str;
	}
	public String getInspectorID(){
		return InspectorID;
	}
	
	public void setInspectorName(String str){
		InspectorName=str;
	}
	public String getInspectorName(){
		return InspectorName;
	}
	
	public void setResolveDatePictureURL1(String str){
		ResolveDatePictureURL1=str;
	}
	public String getResolveDatePictureURL1(){
		return ResolveDatePictureURL1;
	}
	
	public void setResolveDatePictureURL2(String str){
		ResolveDatePictureURL2=str;
	}
	public String getResolveDatePictureURL2(){
		return ResolveDatePictureURL2;
	}
	
	public void setResolveDatePictureURL3(String str){
		ResolveDatePictureURL3=str;
	}
	public String getResolveDatePictureURL3(){
		return ResolveDatePictureURL3;
	}
	
	public void setReInspectedUnresolvedDate(String str){
		ReInspectedUnresolvedDate=str;
	}
	public String getReInspectedUnresolvedDate(){
		return ReInspectedUnresolvedDate;
	}
	
	public void setReInspectedUnresolvedDatePictureURL1(String str){
		ReInspectedUnresolvedDatePictureURL1=str;
	}
	public String getReInspectedUnresolvedDatePictureURL1(){
		return ReInspectedUnresolvedDatePictureURL1;
	}

	public void setReInspectedUnresolvedDatePictureURL2(String str){
		ReInspectedUnresolvedDatePictureURL2=str;
	}
	public String getReInspectedUnresolvedDatePictureURL2(){
		return ReInspectedUnresolvedDatePictureURL2;
	}
	
	public void setReInspectedUnresolvedDatePictureURL3(String str){
		ReInspectedUnresolvedDatePictureURL3=str;
	}
	public String getReInspectedUnresolvedDatePictureURL3(){
		return ReInspectedUnresolvedDatePictureURL3;
	}
}
