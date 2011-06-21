package de.uniluebeck.itm.mdc.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import de.uniluebeck.itm.mdc.persistence.PluginConfigurationRepository;
import de.uniluebeck.itm.mdc.service.PluginConfiguration.Mode;
import de.uniluebeck.itm.mdcf.PluginIntent;

public class TransferManager {

	private final Context context;
	
	private final PluginConfigurationRepository repository;
	
	private final AlarmManager alarmManager;
	
	public TransferManager(Context context, PluginConfigurationRepository repository) {
		this.context = context;
		this.repository = repository;
		this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}
	
	public void schedule(PluginConfiguration configuration) {
		Mode mode = configuration.getMode();
		if (Mode.ACTIVATED.equals(mode)) {
			activated(configuration);
		} else if(Mode.DEACTIVATED.equals(mode)) {
			deactivated(configuration);
		}
	}
	
	private void activated(PluginConfiguration configuration) {
		configuration.setLastActivated(System.currentTimeMillis());
		repository.store(configuration);
		
		// Set alarm
		long interval = configuration.getPluginInfo().getTransferInterval();
		long total = configuration.getTotalActivationTime();
		long now = System.currentTimeMillis();
		long triggerAtTime = now + (interval - total);
		PendingIntent intent = createIntent(configuration);
		alarmManager.set(AlarmManager.RTC, triggerAtTime, intent);
	}
	
	private void deactivated(PluginConfiguration configuration) {
		long total = configuration.getTotalActivationTime();
		long now = System.currentTimeMillis();
		long lastActivated = configuration.getLastActivated();
		configuration.setTotalActivationTime(total + (now - lastActivated));
		repository.store(configuration);
		
		remove(configuration);
	}
	
	public void remove(PluginConfiguration configuration) {
		PendingIntent intent = createIntent(configuration);
		alarmManager.cancel(intent);
	}
	
	private PendingIntent createIntent(PluginConfiguration configuration) {
		Intent intent = new Intent(context, PluginService.class);
		intent.setAction(PluginService.TRANSFER_REQUEST);
		intent.putExtra(PluginIntent.PLUGIN_INFO, configuration.getPluginInfo());
		return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
	}
}
