/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.rendering.quads.tests;

import de.d2dev.fourseasons.resource.DummyResourceLocator;
import de.d2dev.heroquest.engine.rendering.quads.JmeRenderer;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;

/**
 * TestMain
 * @author Justus
 */
public class Main {
	
    public static void main(String[] args) {
        QuadRenderModel map = new TestMap ();
        JmeRenderer stupidRenderer = new JmeRenderer(map);
        stupidRenderer.start();
    }
}
