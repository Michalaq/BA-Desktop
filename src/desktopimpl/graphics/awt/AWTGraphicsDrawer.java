package desktopimpl.graphics.awt;

import gameengine.graphicengine.BackgroundGraphicData;
import gameengine.graphicengine.CircularBackgroundGraphicData;
import gameengine.graphicengine.CustomShapeBackgroundGraphicData;
import gameengine.graphicengine.GraphicsDrawer;
import gameengine.graphicengine.RectangularBackgroundGraphicData;
import gameengine.graphicengine.SpriteObjectGraphicData;
import gameengine.menus.controls.GameText;

import java.awt.Color;
import java.awt.Graphics;

public class AWTGraphicsDrawer extends GraphicsDrawer {
	private Graphics graphics;
	
	public AWTGraphicsDrawer(float screenWidth, float screenHeight) {
		super(screenWidth, screenHeight);
	}

	public void setCanvas(Graphics g) {
		this.graphics = g;
	}
	
	@Override
	public void drawSpriteObject(SpriteObjectGraphicData wogd) {
		((AWTSpriteObjectGraphicData) wogd).draw(graphics);
		
	}

	@Override
	public void drawBackground(BackgroundGraphicData bgd) {
		((AWTBackgroundGraphicData) bgd).draw(graphics);

	}

	@Override
	public void drawCircularBackground(CircularBackgroundGraphicData cbgd) {
		((AWTCircularBackgroundGraphicData) cbgd).draw(graphics);

	}

	@Override
	public void drawRectangularBackground(RectangularBackgroundGraphicData rbgd) {
		((AWTRectangularBackgroundGraphicData) rbgd).draw(graphics);

	}

	@Override
	public void drawCustomShapeBackground(CustomShapeBackgroundGraphicData csbgd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawText(GameText text) {
		graphics.setColor(Color.RED);
		graphics.drawString(text.getText(), (int) text.getX(), (int) text.getY());

	}

}
