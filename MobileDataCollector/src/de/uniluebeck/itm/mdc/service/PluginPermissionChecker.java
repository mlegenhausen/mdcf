package de.uniluebeck.itm.mdc.service;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

public class PluginPermissionChecker {
	
	private static final String[] DENIED_PERMISSIONS = {
		Manifest.permission.ACCESS_COARSE_LOCATION,
		Manifest.permission.ACCESS_FINE_LOCATION,
		Manifest.permission.ACCESS_NETWORK_STATE,
		Manifest.permission.ACCESS_WIFI_STATE,
		Manifest.permission.BLUETOOTH_ADMIN,
		Manifest.permission.CAMERA,
		Manifest.permission.INSTALL_LOCATION_PROVIDER,
		Manifest.permission.INTERNET,
		Manifest.permission.READ_PHONE_STATE,
		Manifest.permission.RECORD_AUDIO
	};

	private final PackageManager packageManager;
	
	public PluginPermissionChecker(Context context) {
		this.packageManager = context.getPackageManager();
	}
	
	public PluginConfiguration updatePermissions(PluginConfiguration configuration) {
		List<String> permissions = new ArrayList<String>();
		String pkg = configuration.getPluginInfo().getPackage();
		for (String permission : DENIED_PERMISSIONS) {
			int access = packageManager.checkPermission(permission, pkg);
			if (access == PackageManager.PERMISSION_GRANTED) {
				permissions.add(permission);
			}
		}
		configuration.setPermissions(permissions);
		return configuration;
	}
}
