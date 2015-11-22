package desktopimpl.graphics.awt;

import gameengine.GameEngine;
import gameengine.graphicengine.CircularBackgroundGraphicData;
import gameengine.objects.TextureObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class AWTCircularBackgroundGraphicData extends
		CircularBackgroundGraphicData {
	private BufferedImage texture;
	private TexturePaint tp;
	private Ellipse2D e;
	public AWTCircularBackgroundGraphicData(BufferedImage texture, float x, float y, float radius) {
		super(x, y, radius);
		e = new Ellipse2D.Float(x, y, radius, radius);
		this.texture = texture;
		tp = new TexturePaint(texture, new Rectangle(0, 0, texture.getWidth(), texture.getHeight()));
	}

	@Override
	public void update(TextureObject to) {
		// TODO Auto-generated method stub

	}

	public void draw(Graphics g) {
		Graphics2D gg = (Graphics2D)g;
		GameEngine.sendDebugMessage("Ko³ko", String.format("Pozycja: %f %f", x, y));
		gg.setPaint(tp);
		gg.drawOval((int) (x - radius), (int) (y - radius), (int) radius*2, (int) radius*2);
		gg.fillOval((int) (x - radius),(int) (y - radius), (int) radius*2,(int) radius*2);
		gg.setPaint(null);
	}
}
