package desktopimpl;

import gameengine.GameEngine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import desktopimpl.graphics.awt.AWTGraphicsDrawer;

public class LevelView extends Canvas {
	private GameEngine gameEngine;
	private AWTGraphicsDrawer graphicsDrawer;
	private Image offscreen;
	private Graphics buffer;
	private Dimension dim;
	
	public void setDrawingUtilities(GameEngine gameEngine, AWTGraphicsDrawer graphicsDrawer) {
		this.gameEngine = gameEngine;
		this.graphicsDrawer = graphicsDrawer;
		dim = getSize();
		offscreen = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
		buffer = offscreen.getGraphics();
		//setBackground(Color.CYAN);
	}
	
	@Override
	public void paint(Graphics g) {
		buffer.setFont(g.getFont());
		buffer.clearRect(0, 0, dim.width, dim.height);
		buffer.setColor(Color.BLACK);
		if (gameEngine != null) {
			graphicsDrawer.setCanvas(buffer);
			gameEngine.getGameScene().draw(graphicsDrawer);
		}
		g.drawImage(offscreen, 0, 0, this);
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
}
