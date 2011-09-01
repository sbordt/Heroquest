/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.Unit;

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
