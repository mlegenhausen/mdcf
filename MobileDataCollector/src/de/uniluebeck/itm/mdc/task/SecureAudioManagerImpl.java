package de.uniluebeck.itm.mdc.task;

import android.media.AudioManager;
import de.uniluebeck.itm.mdcf.service.SecureAudioManager;

public class SecureAudioManagerImpl extends SecureAudioManager.Stub {

	private AudioManager audioManager;
	
	public SecureAudioManagerImpl(AudioManager audioManager) {
		this.audioManager = audioManager;
	}

}
