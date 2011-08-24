/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.rendering.stupid;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Justus
 */
public class TestMap implements StupidRenderModel {

    public int getWidth() {
        return 20;
    }

    public int getHeight() {
        return 30;
    }

    public List<RenderQuad> getQuads() {
        List<RenderQuad> list = new ArrayList<RenderQuad>();
        for (int i = 0; i < 20; i++){
            for (int j = 0; j < 30; j++){
                list.add(new RenderQuad(i,j,1,1,0,"Textures/Feld.bmp"));
            }
        }
        return list;
    }
    
}
