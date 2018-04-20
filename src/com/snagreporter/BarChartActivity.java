/* ===========================================================
 * AFreeChart : a free chart library for Android(tm) platform.
 *              (based on JFreeChart and JCommon)
 * ===========================================================
 *
 * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:
 *    AFreeChart: http://code.google.com/p/afreechart/
 *    JFreeChart: http://www.jfree.org/jfreechart/index.html
 *    JCommon   : http://www.jfree.org/jcommon/index.html
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * [Android is a trademark of Google Inc.]
 *
 * -----------------
 * BarChartDemo01Activity.java
 * -----------------
 * (C) Copyright 2010, 2011, by ICOMSYSTECH Co.,Ltd.
 *
 * Original Author:  Niwano Masayoshi (for ICOMSYSTECH Co.,Ltd);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 19-Nov-2010 : Version 0.0.1 (NM);
 */

package com.snagreporter;




import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.CategoryAxis;
import org.afree.chart.axis.CategoryLabelPositions;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.CategoryPlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.renderer.category.BarRenderer;
import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;

import com.snagreporter.database.FMDBDatabaseAccess;
import com.snagreporter.entity.ApartmentMaster;
import com.snagreporter.entity.BuildingMaster;
import com.snagreporter.entity.FloorMaster;
import com.snagreporter.entity.ProjectMaster;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/**
 * BarChartDemo01Activity
 */
public class BarChartActivity extends Activity {

	String currentprojectID;
	BuildingMaster currentBuilding;
	FloorMaster currentFloor;
	ApartmentMaster CurrentAPT;
	Boolean isProject=false,isApt=false,isBuild=false,isFloor=false,isoneAPT=false;
    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     */
	CategoryDataset dataset;
	AFreeChart chart;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //  arrProjects=(ProjectMaster[])getIntent().getExtras().get("project");
        try{
        isProject=getIntent().getExtras().getBoolean("project");
        isBuild=getIntent().getExtras().getBoolean("building");
        isFloor=getIntent().getExtras().getBoolean("floor");
        isApt=getIntent().getExtras().getBoolean("apartment");
        isoneAPT=getIntent().getExtras().getBoolean("Oneapartment");
        if(isProject)
        {
        	dataset = createDataset();
        	chart = createChart(dataset,"Project");
        }
        else if(isBuild)
        {
        	currentprojectID=getIntent().getExtras().getString("currentprojectID");
        	dataset = createBuildingDataset();
        	chart = createChart(dataset,"Building");
        }
        else if(isFloor)
        {
        	currentBuilding=(BuildingMaster)getIntent().getExtras().get("currentBuilding");
        	dataset=createFloorDataset();
        	chart=createChart(dataset,"Floor");;
        }
        else if(isApt)
        {
        	currentFloor=(FloorMaster)getIntent().getExtras().get("currentfloor");	
        	dataset=createAptDataset();
        	chart=createChart(dataset,"Apartment");
        }
        else if(isoneAPT){
        	CurrentAPT=(ApartmentMaster)getIntent().getExtras().get("CurrentAPT");
        	dataset=createOneAptDataset();
        	chart=createChart(dataset,"Apartment");
        }
        BarChartView mView = new BarChartView(this,dataset,chart);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mView);
        }
		catch(Exception e)
		{
			Log.d("Exception", ""+e.getMessage());
		}
        
    }
    private  CategoryDataset createDataset() 
    {

    	
    	ProjectMaster[] arrProjects;
    	FMDBDatabaseAccess fdb=new FMDBDatabaseAccess(BarChartActivity.this);
    	arrProjects=fdb.getProjects();
    	
        // row keys...
        String series1 = "Resolved";
        String series2 = "UnResolved";
   // column keys...
        String category[]=new String[arrProjects.length];
        for(int i=0;i<arrProjects.length;i++)
        {
        	category[i]=arrProjects[i].getProjectName();
        }
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int resolveValu[]=new int[arrProjects.length];
        for(int i=0;i<arrProjects.length;i++)
        {
        	resolveValu[i]=fdb.getResolvedSnagsByProject(arrProjects[i].getID(),"");
        }
        for(int i=0;i<arrProjects.length;i++)
        {
        	 dataset.addValue(resolveValu[i], series1, category[i]);
        }

       int unresolveValu[]=new int[arrProjects.length];
        for(int i=0;i<arrProjects.length;i++)
        {
        	unresolveValu[i]=fdb.getUnresolvedSnagsByProject(arrProjects[i].getID(),"");
        }
        
        for(int i=0;i<arrProjects.length;i++)
        {
        	 dataset.addValue(unresolveValu[i], series2, category[i]);
        }


        return dataset;

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private  AFreeChart createChart(CategoryDataset dataset,String strFrom) 
    {

        // create the chart...
        AFreeChart chart = ChartFactory.createBarChart(
            "Resolved vs Unresolved",      // chart title
            strFrom,               // domain axis label
            "No. of Snags",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientColor gp0 = new GradientColor(Color.GREEN, Color.rgb(0, 0, 64));
        GradientColor gp1 = new GradientColor(Color.RED, Color.rgb(0, 64, 0));
        GradientColor gp2 = new GradientColor(Color.RED, Color.rgb(64, 0, 0));
        renderer.setSeriesPaintType(0, gp0);
        renderer.setSeriesPaintType(1, gp1);
        renderer.setSeriesPaintType(2, gp2);
        

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }
    private  CategoryDataset createBuildingDataset()
    {
    	
    	BuildingMaster[] arrBuild;
    	FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(BarChartActivity.this);
    	arrBuild=fmdb.getBuildings(currentprojectID);
    	  String series1 = "Resolved";
          String series2 = "UnResolved";
          
          String category[]=new String[arrBuild.length]; 
          
          for(int i=0;i<arrBuild.length;i++)
          {
          	category[i]=arrBuild[i].getBuildingName();
          }
          
          
          DefaultCategoryDataset dataset = new DefaultCategoryDataset();
          int resolveValu[]=new int[arrBuild.length];
          
          for(int i=0;i<arrBuild.length;i++)
          {
          	resolveValu[i]=fmdb.getResolvedSnagsByBuilding(currentprojectID, arrBuild[i].getID(),"");
          }
          for(int i=0;i<arrBuild.length;i++)
          {
          	 dataset.addValue(resolveValu[i], series1, category[i]);
          }

         int unresolveValu[]=new int[arrBuild.length];
          for(int i=0;i<arrBuild.length;i++)
          {
          	unresolveValu[i]=fmdb.getUnresolvedSnagsByBuilding(currentprojectID, arrBuild[i].getID(),"");
          }
          
          for(int i=0;i<arrBuild.length;i++)
          {
          	 dataset.addValue(unresolveValu[i], series2, category[i]);
          }

          
          return dataset;

          
    	
    	
    }
   
    private  CategoryDataset createFloorDataset() 
    {
    	FloorMaster[] arrFloor;
    	FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(BarChartActivity.this);
    	arrFloor=fmdb.getFloors(currentBuilding);
    	  String series1 = "Resolved";
          String series2 = "UnResolved";
          
          String category[]=new String[arrFloor.length]; 
          
          for(int i=0;i<arrFloor.length;i++)
          {
          	category[i]=arrFloor[i].getFloor();
          }
          
          
          DefaultCategoryDataset dataset = new DefaultCategoryDataset();
          int resolveValu[]=new int[arrFloor.length];
          
          for(int i=0;i<arrFloor.length;i++)
          {
          	resolveValu[i]=fmdb.getResolvedSnagsByFloor(currentBuilding.getProjectID(), currentBuilding.getID(),arrFloor[i].getID(),"");
          }
          for(int i=0;i<arrFloor.length;i++)
          {
          	 dataset.addValue(resolveValu[i], series1, category[i]);
          }

         int unresolveValu[]=new int[arrFloor.length];
          for(int i=0;i<arrFloor.length;i++)
          {
          	unresolveValu[i]=fmdb.getUnresolvedSnagsByFloor(currentBuilding.getProjectID(), currentBuilding.getID(), arrFloor[i].getID(), "");
          }
          
          for(int i=0;i<arrFloor.length;i++)
          {
          	 dataset.addValue(unresolveValu[i], series2, category[i]);
          }


          return dataset;

          
    	
    }
   
    private  CategoryDataset createAptDataset() 
    {
    	ApartmentMaster[] arrApt;
    	FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(BarChartActivity.this);
    	arrApt=fmdb.getApartments(currentFloor);
    	String series1 = "Resolved";
        String series2 = "UnResolved";
       
        String category[]=new String[arrApt.length]; 
        
        for(int i=0;i<arrApt.length;i++)
        {
        	category[i]=arrApt[i].getApartmentNo();
        }
        
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int resolveValu[]=new int[arrApt.length];
        
        for(int i=0;i<arrApt.length;i++)
        {
        	resolveValu[i]=fmdb.getResolvedSnagsByApt(currentFloor.getProjectID(),currentFloor.getBuildingID(), currentFloor.getID(),arrApt[i].getID(),"");
        }
        for(int i=0;i<arrApt.length;i++)
        {
        	 dataset.addValue(resolveValu[i], series1, category[i]);
        }

       int unresolveValu[]=new int[arrApt.length];
        for(int i=0;i<arrApt.length;i++)
        {
        	unresolveValu[i]=fmdb.getUnresolvedSnagsByApt(currentFloor.getProjectID(),currentFloor.getBuildingID(),currentFloor.getID(),arrApt[i].getID(),"");
        }
        
        for(int i=0;i<arrApt.length;i++)
        {
        	 dataset.addValue(unresolveValu[i], series2, category[i]);
        }


        return dataset;

    
    }
    private  CategoryDataset createOneAptDataset() 
    {
    	ApartmentMaster[] arrApt;
    	FMDBDatabaseAccess fmdb=new FMDBDatabaseAccess(BarChartActivity.this);
    	arrApt=new ApartmentMaster[1];
    	arrApt[0]=CurrentAPT;
    	String series1 = "Resolved";
        String series2 = "UnResolved";
       
        String category[]=new String[arrApt.length]; 
        
        for(int i=0;i<arrApt.length;i++)
        {
        	category[i]=arrApt[i].getApartmentNo();
        }
        
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int resolveValu[]=new int[arrApt.length];
        
        for(int i=0;i<arrApt.length;i++)
        {
        	resolveValu[i]=fmdb.getResolvedSnagsByApt(CurrentAPT.getProjectID(),CurrentAPT.getBuildingID(), CurrentAPT.getFloorID(),arrApt[i].getID(),"");
        }
        for(int i=0;i<arrApt.length;i++)
        {
        	 dataset.addValue(resolveValu[i], series1, category[i]);
        }

       int unresolveValu[]=new int[arrApt.length];
        for(int i=0;i<arrApt.length;i++)
        {
        	unresolveValu[i]=fmdb.getUnresolvedSnagsByApt(CurrentAPT.getProjectID(),CurrentAPT.getBuildingID(),CurrentAPT.getFloorID(),arrApt[i].getID(),"");
        }
        
        for(int i=0;i<arrApt.length;i++)
        {
        	 dataset.addValue(unresolveValu[i], series2, category[i]);
        }


        return dataset;

    
    }
  
    
}
