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

public class TestResultView extends LinearLayout {

	public static final int NONE = -1;
	public static final int FAIL = 1;
	public static final int SUCCESS = 2;
	public static final int SEMI = 3;
	public String description;
	
	public TextView descriptionview;
	public ImageView statusview;
	
	
	public TestResultView(Context context, String description) {
		super(context);
		this.description = description;
		setViews();
	}
	
	
	public void setStatus(int status, boolean invert) {
		if (invert) {
			if (status == Result.SUCCESS)
				status = TestResultView.FAIL;
			else if (status == Result.FAILURE)
				status = TestResultView.SUCCESS;
			else if (status == Result.SEMI) {
				status = TestResultView.SEMI;
			} else {
				status = TestResultView.NONE;
			}
		}
		
		if (status == NONE) {
			statusview.setVisibility(View.VISIBLE);
			statusview.setImageResource(R.drawable.untested);
		} else if (status == FAIL) {
			statusview.setVisibility(View.VISIBLE);
			statusview.setImageResource(R.drawable.bad);
		} else if (status == SUCCESS) {
			statusview.setVisibility(View.VISIBLE);
			statusview.setImageResource(R.drawable.good);
		} else if (status == SEMI) {
			statusview.setVisibility(View.VISIBLE);
			statusview.setImageResource(R.drawable.semi);
		}

	}
	
	

	
	public void setViews() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup layout = (ViewGroup)inflater.inflate(R.layout.test_details_item, this, false);
		this.addView(layout);
		
		descriptionview  = (TextView)layout.findViewById(R.id.attempt_details);
		statusview  = (ImageView)layout.findViewById(R.id.attempt_result);

		descriptionview.setText(description);
		
		setStatus(NONE, false);
	}
	
	


}
