package desktopimpl.graphics.awt;

import gameengine.Coordinates;
import gameengine.graphicengine.BackgroundGraphicData;
import gameengine.graphicengine.CircularBackgroundGraphicData;
import gameengine.graphicengine.CustomShapeBackgroundGraphicData;
import gameengine.graphicengine.GraphicDataFactory;
import gameengine.graphicengine.RectangularBackgroundGraphicData;
import gameengine.graphicengine.SpriteObjectGraphicData;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class AWTGraphicDataFactory extends GraphicDataFactory {
	private final String graphicsPath;
	private static final String bitmapExtension = ".png";
	private static final boolean scaleSprites = false;
	private static final boolean scaleTextures = true;
	private static Map<String, BufferedImage> sprites;
	private static Map<String, BufferedImage> textures;

	public AWTGraphicDataFactory(String graphicsPath) {
		this.graphicsPath = graphicsPath;
		sprites = new HashMap<String, BufferedImage>();
		textures = new HashMap<String, BufferedImage>();
	}
	
	@Override
	protected BufferedImage getSpriteResource(String sprite) {
		return sprites.get(sprite);
	}

	@Override
	protected BufferedImage getResizedSpriteResource(String sprite, float xResized,
			float yResized) {
		//TODO
		return getSpriteResource(sprite);
	}

	@Override
	protected BufferedImage getResizedSpriteResource(String sprite, int toWidth,
			int toHeight) {
		// TODO Auto-generated method stub
		return getSpriteResource(sprite);
	}

	@Override
	protected BufferedImage getTextureResource(String texture) {
		return textures.get(texture);
	}

	@Override
	protected void loadSprite(String sprite) {
		System.out.println(graphicsPath + sprite.toLowerCase() + bitmapExtension);
		File img = new File(graphicsPath + sprite.toLowerCase() + bitmapExtension);
        try {
			sprites.put(sprite, ImageIO.read(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}

	@Override
	protected void loadTexture(String texture) {
		File img = new File(graphicsPath + texture.toLowerCase() + bitmapExtension);
        try {
			textures.put(texture, ImageIO.read(img));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public SpriteObjectGraphicData createSpriteObjectGraphicData(String sprite) {
		return new AWTSpriteObjectGraphicData(getSpriteResource(sprite));
	}

	@Override
	public SpriteObjectGraphicData createResizedSpriteObjectGraphicData(
			String sprite, int toWidth, int toHeight) {
		// TODO Auto-generated method stub
		return createSpriteObjectGraphicData(sprite);
	}

	@Override
	public SpriteObjectGraphicData createResizedSpriteObjectGraphicData(
			String sprite, float xResized, float yResized) {
		// TODO Auto-generated method stub
		return createSpriteObjectGraphicData(sprite);
	}

	@Override
	public BackgroundGraphicData createSceneryGraphicData(String texture) {
		// TODO Auto-generated method stub
		return new AWTBackgroundGraphicData(getTextureResource(texture));
	}

	@Override
	public CircularBackgroundGraphicData createCircularFieldGraphicData(
			String texture, float x, float y, float radius) {
		// TODO Auto-generated method stub
		return new AWTCircularBackgroundGraphicData(getTextureResource(texture), x, y, radius);
	}

	@Override
	public RectangularBackgroundGraphicData createRectangularFieldGraphicData(
			String texture, float x, float y, float xEnd, float yEnd) {
		// TODO Auto-generated method stub
		return new AWTRectangularBackgroundGraphicData(getTextureResource(texture), x, y, xEnd, yEnd);
	}

	@Override
	public CustomShapeBackgroundGraphicData createCustomShapeFieldGraphicData(
			String texture, List<Coordinates> edgeCoordinates) {
		// TODO Auto-generated method stub
		return null;
	}

}
