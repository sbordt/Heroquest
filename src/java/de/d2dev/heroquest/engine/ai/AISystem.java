/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.game.Unit;
import java.util.Map;

/**
 *
 * @author Simon + Toni
 */
public class AISystem {
    
    private Map map;

    public AISystem(Map map) {
        this.map = map;
    } 
    
    public AIController creatAIController(Unit unit){
        return null;
    }
    
}
