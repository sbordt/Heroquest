/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.d2dev.heroquest.engine.ai;

import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.Monster;
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
        return new AIMonsterController(unit,map);
    }
    
    /**
     * Inform the ai system that a monsters round begins. 
     */
    public void startMonstersRound() {
    }
    
    /**
     * During a monsters round, this method determines
     * the next monster to act. Each monster can only
     * act once. To get the monsters actions, its 
     * {@link AIController} will be asked. This methods allows the ai
     * to determine the order in which the monsters act.
     * @return {@code null} if there are no more monsters to do a thing!
     */
    public Monster getNextMonster() {
    	return null;
    }
    
    /**
     * Inform the ai system that a monsters round ends.
     */
    public void endMonstersRound() {	
    }
    
}
