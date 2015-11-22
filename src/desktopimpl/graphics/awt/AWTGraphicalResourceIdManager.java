package desktopimpl.graphics.awt;

import gameengine.graphicengine.GraphicalResourceIdManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.gson.Gson;

public class AWTGraphicalResourceIdManager extends GraphicalResourceIdManager {
	private static final String extension = ".json";
	private static final String graphicalIdConfigFile = "graphical_config";
	private final String graphicsConfigPath;
	
	public AWTGraphicalResourceIdManager(String graphicsConfigPath) {
		this.graphicsConfigPath = graphicsConfigPath;
	}
	
	class ResourcesIds {
		private String[] spriteIds;
		private String[] textureIds;
	}
	
	@Override
	public void loadIds() {
		try {
			Gson g = new Gson();
			InputStream is = new FileInputStream(graphicsConfigPath + graphicalIdConfigFile + extension); 
			byte[] data;
			data = new byte[is.available()];
			is.read(data); is.close();
			String jsonString = new String(data);
			ResourcesIds rld = g.fromJson(jsonString, ResourcesIds.class);
			AWTGraphicalResourceIdManager.spriteIds = rld.spriteIds;
			AWTGraphicalResourceIdManager.textureIds = rld.textureIds;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
