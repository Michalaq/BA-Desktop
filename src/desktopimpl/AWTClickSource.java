package desktopimpl;

import gameengine.GameEngine;
import gameengine.graphicengine.GraphicsDrawer;
import gameengine.input.ClickSource;
import gameengine.input.Clickable;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AWTClickSource implements ClickSource, MouseListener, MouseMotionListener {
	private static final int max_clickables = 100;
	private PriorityQueue<Clickable> listeners = new PriorityQueue<Clickable>(100,
				new Comparator<Clickable>() {

					@Override
					public int compare(Clickable lhs, Clickable rhs) {
						return rhs.getPriority()-lhs.getPriority();
					}
					
				}
			);
	private PriorityQueue<Clickable> tmp;
	private Clickable c;
	private Collection<Clickable> listenersToRemove = new HashSet<Clickable>();
	private MouseEvent lastEvent;
	
	@Override
	public synchronized void addListener(Clickable c) {
		listeners.add(c);
		GameEngine.sendDebugMessage("Input_3", "Dodaje: " + c.toString());
	}

	@Override
	public synchronized void removeListener(Clickable c) {
		c.setEnabled(false);
		listenersToRemove.add(c);

	}

	@Override
	public synchronized void update(Clickable c) {
		
	}

	@Override
	public synchronized void notifyListeners() {
		float fixedX = lastEvent.getX()/GraphicsDrawer.getGraphicResizeMultiplierX(),
			  fixedY = lastEvent.getY()/GraphicsDrawer.getGraphicResizeMultiplierY();
		System.out.println(String.format("Coordinates: %f, %f.", fixedX, fixedY));
		listeners.removeAll(listenersToRemove);
		listenersToRemove.clear();
		tmp = new PriorityQueue<Clickable>(listeners);
		while(!tmp.isEmpty()) {
			c = tmp.remove();
			if (c.isEnabled()) {
				GameEngine.sendDebugMessage("Input", "Clickable jakis: " + c.toString());
				if (lastEvent.getID() == MouseEvent.MOUSE_PRESSED
					&& c.isInitialPressWithin(fixedX, fixedY)) {
					c.onInitialPress(fixedX,
									fixedY);
					break; // konczy dzialanie po pierwszym napotkanym
				} else switch(lastEvent.getID()) {
					case MouseEvent.MOUSE_DRAGGED : c.onHold(fixedX,
															fixedY); break;
					case MouseEvent.MOUSE_RELEASED : c.onRelease(fixedX,
															 fixedY);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		lastEvent = e;
		notifyListeners();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		lastEvent = e;
		notifyListeners();		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		lastEvent = e;
		notifyListeners();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
}
