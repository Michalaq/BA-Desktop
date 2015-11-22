package desktopimpl;

import gameengine.GameEngine;
import gameengine.graphicengine.GraphicsDrawer;
import gameengine.input.ClickSource;
import gameengine.objects.BackgroundObjectFactory;
import gameengine.objects.SpriteObject;
import gameengine.transformations.Transformation;
import gameengine.transformations.physics.VelocityTransformation;
import gameengine.world.BackgroundScenery;
import gameengine.world.Field;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import balloonadventure.level.BalloonAdventureLevelCreator;
import balloonadventure.level.BalloonAdventureLevelData;
import balloonadventure.level.InLevelBalloonManager;
import balloonadventure.level.RandomBalloonManager;
import balloonadventure.transformations.ElasticCollisionBoxTransformation;
import balloonadventure.transformations.MoveTransformation;
import balloonadventure.transformations.OrbitTransformation;
import balloonadventure.transformations.PulseTransformation;
import balloonadventure.transformations.ShiftVelocityTransformation;
import balloonadventure.world.BalloonType;
import balloonadventure.world.Cloud;
import balloonadventure.world.CloudType;
import balloonadventure.world.WorldObjectFactory;

import com.google.gson.Gson;

public class AWTLevelCreator extends BalloonAdventureLevelCreator {
	private static final String configFile = "level_config",
			levelFilePrefix = "level_";
	private final String configPath;
	private final String extension = ".json";
	private static final Map<String, Constructor> map = new HashMap<String, Constructor>();
	static {
		try {
			map.put("orbit", OrbitTransformation.class.getConstructor(
					float.class, float.class, float.class));
			map.put("box", ElasticCollisionBoxTransformation.class
					.getConstructor(float.class, float.class, float.class,
							float.class, float.class, float.class));
			map.put("velocity", VelocityTransformation.class.getConstructor(
					float.class, float.class));
			map.put("orbit_cloud", OrbitTransformation.class.getConstructor(
					SpriteObject.class, float.class));
			map.put("pulse", PulseTransformation.class.getConstructor(
					float.class, float.class, float.class));
			map.put("move", MoveTransformation.class.getConstructor(
					float.class, float.class, float.class, float.class,
					float.class, float.class));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// plik config: ile jest leveli itd
	/*
	 * plik z leveldata: { "id":#level_id, "layerCount":#layer_count,
	 * "bgScenery":"background_scenery_name", "clouds": [ {
	 * "cloud_type":"spiky", "x":#x, "y":#y, "rangeX":#rangeX, "rangeY":#rangeY
	 * "velocity": { "x":#velX, "y":#velY } }, ... ] "field": {
	 * "field_type":"circular", "texture":"texture", "x":#x, "y":#y, "radius":#r
	 * } }
	 */

	private class TransformationData {
		private String trans_type;
		private float values[];

		@Override
		public String toString() {
			return "[type: " + trans_type + " values: "
					+ Arrays.toString(values) + "]";
		}

		public Transformation<SpriteObject> makeTransformation() {
			try {
				return (Transformation<SpriteObject>) map.get(trans_type)
						.newInstance(getNormalizedValues(values.clone()));
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private Object[] getNormalizedValues(float values[]) {
			GameEngine.sendDebugMessage("TRANS",
					"Value0: " + String.valueOf(values[0]));
			if (trans_type.equals("box") || trans_type.equals("move"))
				return new Object[] { normalizeX(values[0]),
						normalizeY(values[1]), normalizeX(values[2]),
						normalizeY(values[3]),
						normalizeX(values[4]) / GraphicsDrawer.getFPS(),
						normalizeY(values[5]) / GraphicsDrawer.getFPS() };
			if (trans_type.equals("orbit"))
				return new Object[] { normalizeX(values[0]),
						normalizeY(values[1]), values[2] };

			if (trans_type.equals("velocity"))
				return new Object[] { normalizeX(values[0]),
						normalizeY(values[1]) };

			if (trans_type.equals("pulse"))
				return new Object[] { values[0], values[1], values[2] };
			return null;
		}

		private float normalizeX(float x) {
			return x * GraphicsDrawer.getDefaultScreenWidth();
		}

		private float normalizeY(float y) {
			return y * GraphicsDrawer.getDefaultScreenHeight();
		}
	}

	private class CloudData {
		private String cloud_type;
		private float x, y;
		private List<TransformationData> transformations;

		@Override
		public String toString() {
			return String.format("[type: %s, x: %f, y: %f]", cloud_type, x, y);
		}
	}

	private class FieldData {
		private String field_type, texture;
		private float x, y, xEnd, yEnd, radius;

		@Override
		public String toString() {
			return String.format("[type: %s, x: %f, y: %f, radius: %f]",
					field_type, x, y, radius);
		}
	}

	private class RawLevelData {
		private int id;
		private int layerCount;
		private String bgScenery;
		private CloudData clouds[];
		private FieldData field;
		private int timeLeft = -1;

		@Override
		public String toString() {
			return String
					.format("id: %d, layerCount: %d, bgScenery: %s, clouds: %s, field: %s",
							id, layerCount, bgScenery.toString(),
							clouds.toString(), field.toString());
		}
	}

	private class GlobalLevelConfig {
		private int levelCount;
	}

	public AWTLevelCreator(String configPath, ClickSource cs) {
		super(cs);
		this.configPath = configPath;
	}

	private float normalizeX(float x) {
		return x * GraphicsDrawer.getDefaultScreenWidth();
	}

	private float normalizeY(float y) {
		return y * GraphicsDrawer.getDefaultScreenHeight();
	}

	private BalloonAdventureLevelData createDataFromRaw(RawLevelData rld) {
		Collection<Cloud> clouds = new HashSet<Cloud>();
		Cloud c;
		for (CloudData cd : rld.clouds) {
			c = WorldObjectFactory.createCloud(
					CloudType.valueOf(cd.cloud_type), normalizeX(cd.x),
					normalizeY(cd.y) + 64);
			for (TransformationData td : cd.transformations) {
				GameEngine.sendDebugMessage("TRANS", td.toString());
				if (!td.trans_type.equals("orbit_cloud")) {
					c.addTransformation(td.makeTransformation());
				}
			}
			// c.addTransformation(new PulseTransformation(2.0f, 2.0f, 1));
			clouds.add(c);
			// c.addTransformation(new
			// OrbitTransformation(GraphicsDrawer.getDefaultScreenWidth()/2,
			// GraphicsDrawer.getDefaultScreenHeight()/2, 180));
			// c.addTransformation(new
			// ElasticCollisionBoxTransformation(normalizeX(cd.x),
			// normalizeY(cd.y)+64, normalizeX(cd.rangeX),
			// normalizeY(cd.rangeY), vX, vY));
		}
		InLevelBalloonManager bm = new RandomBalloonManager(3, new BalloonType[] {
				BalloonType.BALLOON_GOLD, BalloonType.BALLOON_BLUE,
				BalloonType.BALLOON_RED }, new float[] { 0.05f, 0.2f });
		BackgroundScenery bs = BackgroundObjectFactory
				.createBackgroundScenery(String.valueOf(rld.bgScenery));
		bs.addTransformation(new ShiftVelocityTransformation(2, 0));
		Field f = null;
		if (rld.field.field_type.equals("circular")) {
			f = BackgroundObjectFactory.createCircularField(
					String.valueOf(rld.field.texture), normalizeX(rld.field.x),
					normalizeY(rld.field.y), normalizeX(rld.field.radius));
		} else if (rld.field.field_type.equals("rectangular")) {
			f = BackgroundObjectFactory.createRectangularField(
					String.valueOf(rld.field.texture), normalizeX(rld.field.x),
					normalizeY(rld.field.y), normalizeX(rld.field.xEnd),
					normalizeY(rld.field.yEnd));
		} // asdasdasd
		return new BalloonAdventureLevelData(rld.layerCount, rld.id,
				rld.timeLeft, bs, clouds, f, bm);
	}

	private String getJSONFromFile(String fileName) throws IOException {
		InputStream is;
		is = new FileInputStream(new File(configPath + fileName + extension));
		byte[] data = new byte[is.available()];
		is.read(data);
		is.close();
		String jsonString = new String(data);
		return jsonString;
	}

	private boolean isExternalStorageReadable() {

		return false;
	}

	private String getJSONFromExternalFile(String fileName) {
		if (!isExternalStorageReadable()) {
			return null;
		}
		try {
			FileInputStream fis;
			File f = new File(fileName
					+ ".json");
			fis = new FileInputStream(f);
			byte[] data = new byte[fis.available()];
			fis.read(data);
			fis.close();
			String jsonString = new String(data);
			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void loadLevelData() {
		try {
			Gson g = new Gson();
			String configString = getJSONFromFile(configFile);
			GlobalLevelConfig clg = g.fromJson(configString,
					GlobalLevelConfig.class);
			levelCount = clg.levelCount;
			configString = getJSONFromExternalFile(configFile);
			int additionalLevels = 0;
			if (configString != null) {
				clg = g.fromJson(configString, GlobalLevelConfig.class);
				additionalLevels = clg.levelCount;
			}
			levelData = new BalloonAdventureLevelData[levelCount
					+ additionalLevels + 1];
			String jsonString;
			for (int i = 1; i <= levelCount; i++) {
				jsonString = getJSONFromFile(levelFilePrefix
						+ String.valueOf(i));
				RawLevelData rld = g.fromJson(jsonString, RawLevelData.class);
				levelData[i] = createDataFromRaw(rld);
			}
			for (int i = 1; i <= additionalLevels; i++) {
				jsonString = getJSONFromExternalFile(levelFilePrefix
						+ String.valueOf(i));
				RawLevelData rld = g.fromJson(jsonString, RawLevelData.class);
				rld.id += levelCount;
				levelData[levelCount + i] = createDataFromRaw(rld);
			}
			levelCount += additionalLevels;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
