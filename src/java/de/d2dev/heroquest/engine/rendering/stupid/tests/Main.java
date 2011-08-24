/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.rendering.stupid.tests;

import de.d2dev.heroquest.engine.rendering.stupid.StupidRenderModel;
import de.d2dev.heroquest.engine.rendering.stupid.jmeRenderer;

/**
 * TestMain
 * @author Justus
 */
public class Main {
    public static void main(String[] args) {
        StupidRenderModel map = new TestMap ();
        jmeRenderer stupidRenderer = new jmeRenderer(map);
        stupidRenderer.start();
    }
}
