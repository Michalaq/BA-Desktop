package desktopimpl.game;
import gameengine.Debugger;
import gameengine.GameEngine;
import gameengine.menus.MenuManager;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import desktopimpl.AWTClickSource;
import desktopimpl.AWTGameThread;
import desktopimpl.AWTLevelCreator;
import desktopimpl.LevelView;
import desktopimpl.graphics.awt.AWTGraphicDataFactory;
import desktopimpl.graphics.awt.AWTGraphicalResourceIdManager;
import desktopimpl.graphics.awt.AWTGraphicsDrawer;
import desktopimpl.sound.AWTMusicManager;
import balloonadventure.level.BalloonAdventureLevelCreator;
import balloonadventure.level.BalloonAdventureMenuManager;

public class Game
{
	/* note that if exporting to jar, absolute paths to assets are needed */
	private static final String ASSETS_PATH = ".\\assets\\"; // works in Eclipse
	private static final String SOUND_PATH = ASSETS_PATH + "sounds\\",
								CONFIG_PATH = ASSETS_PATH + "config\\",
								GRAPHIC_CONFIG_PATH = ASSETS_PATH + "graphics\\",
								GRAPHICS_PATH = ASSETS_PATH + "graphics\\"; 
	private static JPanel jpnl;
	private static LevelView levelView;
	private static AWTGraphicsDrawer awtGraphicsDrawer;
	private static AWTGameThread awtGameThread;
	private static AWTClickSource awtClickSource;
	private static AWTLevelCreator awtLevelCreator;
	private static AWTMusicManager awtMusicManager;
	private static MenuManager awtMenuManager;
	private static GameEngine gameEngine;
	
    public static void main(String[] args)
    {
        JFrame frmMain = new JFrame();
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.setSize(480, 864);
        jpnl = new JPanel() {
        	@Override
        	protected void paintComponent(Graphics g) {
        		System.out.println("Rysuje sie ja.");
        	}
        };
        runGame();
        jpnl.add(levelView);
        frmMain.add(jpnl);
        frmMain.setVisible(true);
        
    }
    
    private static void runGame() {
    	
		int width = 480;
		int height = 864;
		levelView = new LevelView();
		levelView.setSize(width, height);
		GameEngine.setDebugger(new Debugger() { 

			@Override
			public void print(String type, String msg) {
				System.out.printf("[%s] %s\n", type, msg);
				
			}
		});
		awtGraphicsDrawer = new AWTGraphicsDrawer((float) width, (float) height);
		awtGameThread = new AWTGameThread(levelView);
		awtMusicManager = new AWTMusicManager(SOUND_PATH);
		awtClickSource = new AWTClickSource();
		levelView.addMouseListener(awtClickSource);
		levelView.addMouseMotionListener(awtClickSource);
		awtLevelCreator = new AWTLevelCreator(CONFIG_PATH, awtClickSource);
		awtMenuManager = new BalloonAdventureMenuManager(awtClickSource, (BalloonAdventureLevelCreator) awtLevelCreator);
		gameEngine = new GameEngine(
					awtGraphicsDrawer,
					awtGameThread,
					new AWTGraphicalResourceIdManager(GRAPHIC_CONFIG_PATH),
					//androidInputManager,
					new AWTGraphicDataFactory(GRAPHICS_PATH),
					awtMusicManager,
					awtClickSource,
					awtMenuManager,
					awtLevelCreator
					);
		gameEngine.start();
		AWTGameThread.init(gameEngine);
		levelView.setDrawingUtilities(gameEngine, awtGraphicsDrawer);
    }
}
