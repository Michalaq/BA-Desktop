package desktopimpl.graphics.awt;

import gameengine.graphicengine.RectangularBackgroundGraphicData;
import gameengine.objects.TextureObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class AWTRectangularBackgroundGraphicData extends
		RectangularBackgroundGraphicData {
	private Rectangle2D rect;
	private TexturePaint tp;
	
	public AWTRectangularBackgroundGraphicData(BufferedImage texture, float x, float y, float xEnd,
			float yEnd) {
		super(x, y, xEnd, yEnd);
		rect = new Rectangle2D.Float(x, y, xEnd, yEnd);
		tp = new TexturePaint(texture, new Rectangle(0, 0, texture.getWidth(), texture.getHeight()));	
	}

	@Override
	public void update(TextureObject to) {
		// TODO Auto-generated method stub

	}

	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D) g;
		gg.setPaint(tp);
		gg.fill(rect);
		g.setColor(Color.BLACK);
	}
}
