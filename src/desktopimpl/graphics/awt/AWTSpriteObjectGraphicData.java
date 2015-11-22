package desktopimpl.graphics.awt;

import gameengine.graphicengine.SpriteObjectGraphicData;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class AWTSpriteObjectGraphicData extends SpriteObjectGraphicData {
	private BufferedImage bitmap;
	
	public AWTSpriteObjectGraphicData(BufferedImage bitmap) {
		this.bitmap = bitmap;
	}
	
	public void draw(Graphics g) {
		g.drawImage(bitmap, (int) x, (int) y, null);
	}
}
