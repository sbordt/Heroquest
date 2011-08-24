/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.rendering.stupid;

/**
 * TestMain
 * @author Justus
 */
public class Main {
    public static void main(String[] args) {
        StupidRenderModel map = new TestMap ();
        StupidRenderer stupidRenderer = new StupidRenderer(map);
    }
}
