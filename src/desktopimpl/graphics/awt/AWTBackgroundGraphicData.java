package desktopimpl.graphics.awt;

import gameengine.graphicengine.BackgroundGraphicData;
import gameengine.graphicengine.GraphicsDrawer;
import gameengine.objects.TextureObject;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public class AWTBackgroundGraphicData extends BackgroundGraphicData {
	private final BufferedImage texture;
	private TexturePaint tp;
	private Rectangle surface = new Rectangle(0, 0, GraphicsDrawer.getDefaultScreenWidth(),
				GraphicsDrawer.getDefaultScreenHeight());
	
	public AWTBackgroundGraphicData(BufferedImage textureBitmap) {
		texture = textureBitmap;
		tp = new TexturePaint(texture, new Rectangle(0, 0, texture.getWidth(), texture.getHeight()));
	}
	
	@Override
	public void update(TextureObject to) {
		// TODO Auto-generated method stub

	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(tp);
		g2.fill(surface);
	}
}
