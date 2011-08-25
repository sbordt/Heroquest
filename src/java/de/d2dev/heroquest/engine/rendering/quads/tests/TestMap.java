/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.rendering.quads.tests;

import java.util.List;
import java.util.ArrayList;

import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;
import de.d2dev.heroquest.engine.rendering.quads.RenderQuad;

/**
 *
 * @author Justus
 */
public class TestMap extends QuadRenderModel {

    public TestMap() {
		super(20, 30);
		
        for (int i = 0; i < 20; i++){
            for (int j = 0; j < 30; j++){
            	this.addQuad( new RenderQuad(i,j,1,1,0,"Textures/Feld.bmp" ) );
            }
        }
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
