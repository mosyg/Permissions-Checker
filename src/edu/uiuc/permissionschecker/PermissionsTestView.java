package edu.uiuc.permissionschecker;

import edu.uiuc.permissionschecker.infocheck.Result;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PermissionsTestView extends LinearLayout {

	public static final int NONE = -1;
	public static final int IN_PROGRESS = 0;
	public static final int FAIL = 1;
	public static final int SUCCESS = 2;
	public static final int SEMI = 3;
	public int drawable;
	public String title;
	public String description;
	public String keyname; 
	
	public ImageView iconview;
	public TextView titleview;
	public TextView descriptionview;
	public ImageView statusview;
	public ProgressBar spinner;
	public View placeholder;
	
	
	public PermissionsTestView(Context context, int drawable, String title, String description, String keyname) {
		super(context);
		this.drawable = drawable;
		this.title = title;
		this.description = description;
		this.keyname = keyname;
		setViews();
	}
	
	
	public void setStatus(int status, boolean invert) {
		if (invert) {
			if (status == Result.SUCCESS)
				status = PermissionsTestView.FAIL;
			else if (status == Result.FAILURE)
				status = PermissionsTestView.SUCCESS;
			else if (status == Result.SEMI) {
				status = PermissionsTestView.SEMI;
			} else {
				status = PermissionsTestView.NONE;
			}
		}
		
		if (status == NONE) {
//			statusview.setVisibility(View.INVISIBLE);
//			spinner.setVisibility(View.INVISIBLE);
			statusview.setVisibility(View.VISIBLE);
			spinner.setVisibility(View.INVISIBLE);
			statusview.setImageResource(R.drawable.untested);
		} else if (status == IN_PROGRESS) {
			statusview.setVisibility(View.INVISIBLE);
			spinner.setVisibility(View.VISIBLE);
		} else if (status == FAIL) {
			statusview.setVisibility(View.VISIBLE);
			spinner.setVisibility(View.INVISIBLE);
			statusview.setImageResource(R.drawable.bad);
		} else if (status == SUCCESS) {
			statusview.setVisibility(View.VISIBLE);
			spinner.setVisibility(View.INVISIBLE);
			statusview.setImageResource(R.drawable.good);
		} else if (status == SEMI) {
			statusview.setVisibility(View.VISIBLE);
			spinner.setVisibility(View.INVISIBLE);
			statusview.setImageResource(R.drawable.semi);
		}

	}
	
	

	
	public void setViews() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup layout = (ViewGroup)inflater.inflate(R.layout.checker_item, this, false);
		this.addView(layout);
		
		iconview  = (ImageView)layout.findViewById(R.id.testicon);
		titleview  = (TextView)layout.findViewById(R.id.testtitle);
		descriptionview  = (TextView)layout.findViewById(R.id.testtext);
		statusview  = (ImageView)layout.findViewById(R.id.testresult);
		spinner  = (ProgressBar)layout.findViewById(R.id.testspinner);
		placeholder  = layout.findViewById(R.id.testplaceholder);
		
		
		iconview.setImageResource(drawable);
		titleview.setText(title);
		descriptionview.setText(description);
		
		setStatus(NONE, false);
	}
	
	

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

}
