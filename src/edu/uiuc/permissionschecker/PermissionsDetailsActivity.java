package edu.uiuc.permissionschecker;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.uiuc.permissionschecker.infocheck.Result;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class PermissionsDetailsActivity extends Activity {

	ViewGroup testlist;
	TextView description;
	ArrayList<PermissionsTestView> views = new ArrayList<PermissionsTestView>();
	Result result;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_details);
      
        Bundle in = getIntent().getExtras();
        result = (Result)in.getSerializable("result");
        int id = in.getInt("id");
        String title = in.getString("title");
        String description = in.getString("description");
        displayResults(id,title,description);
        
        populateAttempts(result);
        
        
        
        
        
        
    }
	
	public void displayResults(int id, String title, String description) {
		LinearLayout layout = (LinearLayout)findViewById(R.id.insert_checker_item);
		PermissionsTestView view = new PermissionsTestView(this, id, title, description, "");
		view.setStatus(result.success, true);
		layout.addView(view);
	}
	
	public void populateAttempts(Result result) {
		testlist = (ViewGroup)findViewById(R.id.details_list);
		for (Attempt a : result.attempts) {
			String message = "";
			if (a.success == Result.FAILURE) {
				message = a.error;
			} else if (a.success == Result.SUCCESS) {
				message = a.data;
			} else if (a.success == Result.SEMI) {
				message = a.data + " Error: "+a.error;
			}
			TestResultView v = new TestResultView(this, message);
			v.setStatus(a.success, true);
			testlist.addView(v);
		}
	}
	
	
	
	
}