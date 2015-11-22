package desktopimpl;

import gameengine.GameEngine;
import gameengine.GameThread;

import java.awt.Canvas;

import desktopimpl.graphics.awt.AWTGraphicsDrawer;

public class AWTGameThread extends GameThread {
	private Canvas surfaceHolder;
	private long previousSystemTime = System.currentTimeMillis(), currentSystemTime;
	private int interval;
	/////////DEBUG
	private long loopStart, loopEnd;
	private float loopDiff = 0, tries = 1;
	/////////DEBUG
	
	public AWTGameThread(Canvas c) {
		this.surfaceHolder = c;
		interval = 1000/AWTGraphicsDrawer.getFPS();
	}
	
	@Override
	public void run() {
			while (gameEngine.isRunning()) {
				try {
            		//if (!surfaceHolder.isCreating()) {
            		//	c = surfaceHolder.lockCanvas();
            		//}               
	                synchronized (surfaceHolder) {
	                	//if (c != null) {
		                	// tu wszystko
		                	//gameEngine.updateInput();
		                	// tu aktualizacje
		                	currentSystemTime = System.currentTimeMillis();
		                	if (currentSystemTime - previousSystemTime >= interval) {
		                		previousSystemTime = currentSystemTime;
		                		gameEngine.getGameScene().update();
		                		gameEngine.updateGameState();
				                //surfaceHolder.invalidate();
				                surfaceHolder.repaint();
				                
		                	}

	                	//}
		              }
	            } finally {
	                
	            }
			}
	}
}
