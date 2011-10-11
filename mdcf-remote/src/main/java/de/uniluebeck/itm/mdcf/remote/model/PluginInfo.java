package de.uniluebeck.itm.mdcf.remote.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PluginInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6097475469038464727L;

    private String pkg;

    private String name;
    
    private String action;
    
    private String version;
    
    private String url;
    
    private int period;
    
    private int duration;
    
    private String description;
    
    private long transferInterval;
    
    private List<String> services = new ArrayList<String>();
    
    private boolean resetWorkspaceAfterTransfer = true;
    
    public PluginInfo() {
		
	}
    
    public PluginInfo(String action, String name) {
    	this.action = action;
    	this.name = name;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPackage() {
		return pkg;
	}
	
	public void setPackage(String pkg) {
		this.pkg = pkg;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public long getTransferInterval() {
		return transferInterval;
	}
	
	public void setTransferInterval(long transferInterval) {
		this.transferInterval = transferInterval;
	}
	
	public List<String> getServices() {
		return services;
	}
	
	public void setServices(List<String> services) {
		this.services = services;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isResetWorkspaceAfterTransfer() {
		return resetWorkspaceAfterTransfer;
	}
	
	public void setResetWorkspaceAfterTransfer(boolean resetWorkspaceAfterTransfer) {
		this.resetWorkspaceAfterTransfer = resetWorkspaceAfterTransfer;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PluginInfo) {
			return action.equals(((PluginInfo) o).getAction());
		}
		return super.equals(o);
	}
}
