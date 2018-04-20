package com.snagreporter.listitems;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.snagreporter.R;;
public class ListItemWithEdit implements Item {
	private final String         str1;
    //private final String         str2;
    private final LayoutInflater inflater;
    private final int Tag;
    private final int status;
    private final boolean hasEditButton;
    private final boolean isContractor;
    public ListItemWithEdit(LayoutInflater inflater, String text1,int Tag,int status,boolean hasEditButton,boolean isFmCntrctr) {
        this.str1 = text1;
        //this.str2 = text2;
        this.inflater = inflater;
        this.Tag=Tag;
        this.status=status;
        this.hasEditButton=hasEditButton;
        this.isContractor=isFmCntrctr;
    }
	
	public int getViewType() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(LayoutInflater inflater, View convertView) {
		// TODO Auto-generated method stub
		View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.row_cell_with_edit, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        ImageView i=(ImageView)view.findViewById(R.id.row_cell_edit_icon);
        
        if(isContractor)
        {
        	if(status==1) //@@accepted
        	{
        		i.setImageResource(R.drawable.conaccepted);
        	}
        	else if(status==2) //@@started
        	{
        		i.setImageResource(R.drawable.contstartednew);
        	}
        	else if(status==3) //@@Ended
        	{
        		i.setImageResource(R.drawable.contendednew);
        	}
        	else if(status==4) //@@Not Accepted
        	{
        		i.setImageResource(R.drawable.connotaccepted);
        	}
        	else if(status==5)//@@pending
        	{
        		i.setImageResource(R.drawable.pendingnew);
        	}
        }
        else
        {
        	if(status==1){
            	i.setImageResource(R.drawable.pendingnew);
            }
            else if(status==2){
            	i.setImageResource(R.drawable.inspectedunresolved);
            }
            else if(status==3){
            	i.setImageResource(R.drawable.resolvednew);
            }
        }
        
        
        Button text1 = (Button) view.findViewById(R.id.row_cell_withedit_text);
        text1.setText(str1);
        text1.setTag(this.Tag);
        Button btn1 = (Button) view.findViewById(R.id.row_cell_withedit_btn);
        if(hasEditButton)
        	btn1.setTag(this.Tag);
        else
        	btn1.setVisibility(View.GONE);
        

        return view;
	}

}
