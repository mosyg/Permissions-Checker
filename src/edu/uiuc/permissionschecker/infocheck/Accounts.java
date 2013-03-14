package edu.uiuc.permissionschecker.infocheck;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class Accounts extends Info {
	public static String NAME = "Accounts";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		Result result = new Result(NAME);
		try {
			AccountManager accountmanager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
			Account[] accounts = accountmanager.getAccounts();
	
	        if (accounts != null) {
	        	for (Account account : accounts)
	        		result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", account.toString()));
	        } else {
	        	result.addAttempt(new Attempt(Result.NORMAL, Result.SEMI, "accounts were null", ""));
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
		
		
		
        return result;
	}

}
