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
			AppSettings settings = new AppSettings(true);
			settings.setWidth(panel.getWidth());
			settings.setHeight(panel.getHeight());
			jmeApp.setSettings(settings);
			JmeCanvasContext canvasContext = (JmeCanvasContext) jmeApp.getContext();
			Dimension dim = new Dimension(panel.getWidth(), panel.getHeight());
			canvasContext.getCanvas().setPreferredSize(dim);
			System.out.println("resized: width: " + panel.getWidth()+ " height: " + panel.getHeight());			
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}};

	public RunJmeCanvasInSwing (final JFrame frame, final A jmeApp){
		this.frame = frame;
		this.jmeApp = jmeApp;
		frame.addComponentListener(componentListener);
		
		
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		AppSettings settings = new AppSettings(true);
		settings.setWidth(640);
		settings.setHeight(480);
		jmeApp.setSettings(settings);
		jmeApp.createCanvas();
		JmeCanvasContext canvasContext = (JmeCanvasContext) jmeApp.getContext();
		Dimension dim = new Dimension(640, 480);
		canvasContext.getCanvas().setPreferredSize(dim);
		
		if ((panel == null) && (frame != null)){
			panel = new JPanel();
			panel.add(canvasContext.getCanvas());
			frame.add(panel);
			frame.pack();
			frame.setVisible(true);
		}
		else if (panel != null){
			panel.add(canvasContext.getCanvas());
		}
		
		jmeApp.startCanvas();
	}

}
