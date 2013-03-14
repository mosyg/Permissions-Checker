package edu.uiuc.permissionschecker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;

public class PackagePermissionManager {

	
	
	public static List<PackageInfo> getAllOfPermission(Context context, String permission) {
		PackageManager packman = context.getPackageManager(); 
		final List<PackageInfo> appinstall =  packman.getInstalledPackages(PackageManager.GET_PERMISSIONS | 
		                                      PackageManager.GET_PROVIDERS);
		List<PackageInfo> out = new ArrayList<PackageInfo>();
		for (PackageInfo info : appinstall) {
			for (PermissionInfo p : info.permissions) {
				if (p.name.equals(permission)) {
					out.add(info);
				}
			}
		}
		return out;
	}
	
	
	
	
	
	
}
