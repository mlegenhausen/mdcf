package de.uniluebeck.itm.mdc.persistence;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

import de.uniluebeck.itm.mdc.service.PluginConfiguration;
import de.uniluebeck.itm.mdcf.PluginInfo;
import de.uniluebeck.itm.mdcf.persistence.Node;
import de.uniluebeck.itm.mdcf.persistence.Property;

public class Db4oHelper {

    private static ObjectContainer oc = null;
    private Context context; 

    /**       
     * @param contex
     */

    public Db4oHelper(Context contex) {
    	context = contex;
    }

    /**
    * Create, open and close the database
    */
    public ObjectContainer db() {
        try {
            if (oc == null || oc.ext().isClosed()) {
            	oc = Db4oEmbedded.openFile(dbConfig(), db4oDBFullPath(context));
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
    private EmbeddedConfiguration dbConfig() throws IOException {
    	EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
    	configuration.common().objectClass(PluginConfiguration.class);
    	configuration.common().objectClass(PluginInfo.class);
    	configuration.common().objectClass(Node.class).objectField("id").indexed(true);
    	configuration.common().objectClass(Node.class).cascadeOnDelete(true);
    	configuration.common().objectClass(Node.class).cascadeOnUpdate(true);
    	configuration.common().objectClass(Node.class).cascadeOnActivate(true);
    	configuration.common().objectClass(Property.class).objectField("id").indexed(true);
    	configuration.common().objectClass(Property.class).cascadeOnDelete(true);
    	configuration.common().objectClass(Property.class).cascadeOnUpdate(true);
    	configuration.common().objectClass(Property.class).cascadeOnActivate(true);
    	return configuration;
	}

    /**
     * Returns the path for the database location
     */
    private String db4oDBFullPath(Context contex) {
    	return contex.getDir("data", 0) + "/" + "plugin.db4o";
    }

    /**
     * Closes the database
     */
    public void close() {
	   if (oc != null)
           oc.close();
	}
}
