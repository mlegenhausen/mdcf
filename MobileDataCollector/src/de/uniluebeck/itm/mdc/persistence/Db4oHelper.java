package de.uniluebeck.itm.mdc.persistence;

import static com.google.common.collect.Maps.newHashMap;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

import de.uniluebeck.itm.mdc.log.LogEntry;
import de.uniluebeck.itm.mdc.log.LogRecord;
import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdc.service.Transfer;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.persistence.Node;
import de.uniluebeck.itm.mdcf.persistence.Property;

public class Db4oHelper {

    private final static Map<String, ObjectContainer> containers = newHashMap();
    private final Context context;
    private final String path;

    /**       
     * @param contex
     */

    public Db4oHelper(Context contex, String path) {
    	this.context = contex;
    	this.path = path;
    }

    /**
    * Create, open and close the database
    */
    public ObjectContainer db() {
    	ObjectContainer oc = containers.get(path);
        try {
            if (oc == null || oc.ext().isClosed()) {
            	oc = Db4oEmbedded.openFile(dbConfig(), db4oDBFullPath(context));
            	containers.put(path, oc);
            }
            return oc;
        } catch (Exception ie) {
            Log.e(Db4oHelper.class.getName(), ie.toString());
            return null;
        }
    }

    /**
     * Configure the behavior of the database
     */
    protected EmbeddedConfiguration dbConfig() throws IOException {
    	EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
    	
    	configuration.common().objectClass(PluginConfiguration.class).objectField("pluginInfo").indexed(true);
    	configuration.common().objectClass(PluginConfiguration.class).cascadeOnUpdate(true);
    	configuration.common().objectClass(PluginConfiguration.class).cascadeOnDelete(true);
    	
    	configuration.common().objectClass(PluginInfo.class).objectField("action").indexed(true);
    	
    	configuration.common().objectClass(Node.class).objectField("id").indexed(true);
    	configuration.common().objectClass(Node.class).cascadeOnDelete(true);
    	configuration.common().objectClass(Node.class).cascadeOnUpdate(true);
    	configuration.common().objectClass(Node.class).cascadeOnActivate(true);
    	
    	configuration.common().objectClass(Property.class).objectField("id").indexed(true);
    	configuration.common().objectClass(Property.class).cascadeOnDelete(true);
    	configuration.common().objectClass(Property.class).cascadeOnUpdate(true);
    	configuration.common().objectClass(Property.class).cascadeOnActivate(true);
    	
    	configuration.common().objectClass(LogRecord.class).objectField("id").indexed(true);
    	configuration.common().objectClass(LogRecord.class).cascadeOnUpdate(true);
    	configuration.common().objectClass(LogRecord.class).cascadeOnDelete(true);
    	
    	configuration.common().objectClass(LogEntry.class).objectField("id").indexed(true);
    	
    	configuration.common().objectClass(Transfer.class).cascadeOnActivate(true);
    	configuration.common().objectClass(Transfer.class).cascadeOnDelete(true);
    	configuration.common().objectClass(Transfer.class).cascadeOnUpdate(true);
    	
    	return configuration;
	}

    /**
     * Returns the path for the database location
     */
    private String db4oDBFullPath(Context contex) {
    	return contex.getDir("data", 0) + File.pathSeparator + path;
    }

    /**
     * Closes the database
     */
    public void close() {
    	ObjectContainer oc = containers.get(path);
    	if (oc != null) {
    		oc.close();
    	}
	}
}
