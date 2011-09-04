package de.d2dev.heroquest.engine.rendering.quads;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

public class RunJmeCanvasInSwing<A extends SimpleApplication & JmeResizeableApp> implements Runnable {
	
	private JFrame frame = null;
	private JPanel panel = null;
	private A jmeApp = null;
	private JmeCanvasContext canvasContext;
	private ComponentListener componentListener = new ComponentListener (){

		@Override
		public void componentHidden(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
			
		}

		@Override
		public void componentResized(ComponentEvent arg0) {
			jmeApp.onResize(panel.getWidth(), panel.getHeight());
			canvasContext.getCanvas().setSize(panel.getWidth(), panel.getHeight());
			Dimension dim = new Dimension(panel.getWidth(), panel.getHeight());
			canvasContext.getCanvas().setPreferredSize(dim);
			jmeApp.restart();
			System.out.println("resized panel: width: " + panel.getWidth()+ " height: " + panel.getHeight());
			System.out.println("resized panel: x: " + panel.getX()+ " y: " + panel.getY());
			System.out.println("context: width: " + jmeApp.getContext().getSettings().getWidth()+ " height: " + jmeApp.getContext().getSettings().getHeight());
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}};

	public RunJmeCanvasInSwing (final JFrame frame, final A jmeApp){
		this.frame = frame;
		this.jmeApp = jmeApp;
		this.panel = new JPanel();
		frame.add(panel);
		this.panel.addComponentListener(componentListener);
	}
	
	public RunJmeCanvasInSwing (final JPanel panel, final A jmeApp){
		this.panel = panel;
		this.jmeApp = jmeApp;
		this.panel.addComponentListener(componentListener);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		jmeApp.setPauseOnLostFocus(false);
		AppSettings settings = new AppSettings(true);
		settings.setWidth(640);
		settings.setHeight(480);
		jmeApp.setSettings(settings);
		jmeApp.createCanvas();
		canvasContext = (JmeCanvasContext) jmeApp.getContext();
		Dimension dim = new Dimension(640, 480);
		canvasContext.getCanvas().setPreferredSize(dim);
		
		if (frame != null){
			panel.add(canvasContext.getCanvas());
			frame.pack();
			frame.setVisible(true);
		}
		else{
			panel.add(canvasContext.getCanvas());
		}
		
		jmeApp.startCanvas();
	}

}
