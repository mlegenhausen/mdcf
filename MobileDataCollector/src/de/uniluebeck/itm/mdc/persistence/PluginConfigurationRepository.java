package de.uniluebeck.itm.mdc.persistence;

import java.util.List;

import android.content.Context;

import com.db4o.query.Predicate;

import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdcf.PluginInfo;

public class PluginConfigurationRepository extends Repository<PluginConfiguration> {

	public PluginConfigurationRepository(Context ctx) {
		super(PluginConfiguration.class, ctx, "plugin.db4o");
	}

	public PluginConfiguration findByPluginInfo(final PluginInfo info) {
		List<PluginConfiguration> result = db().query(new Predicate<PluginConfiguration>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -4182493642994280806L;

			@Override
			public boolean match(PluginConfiguration configuration) {
				return configuration.getPluginInfo().equals(info);
			}
		});
		return result != null && result.size() > 0 ? result.get(0) : null;
	}
}
