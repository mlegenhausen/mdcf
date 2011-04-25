package de.uniluebeck.itm.mdcf;

interface Plugin {
	
	void init();
	
	void start();
	
	void pause();
	
	void resume();
	
	void stop();
}