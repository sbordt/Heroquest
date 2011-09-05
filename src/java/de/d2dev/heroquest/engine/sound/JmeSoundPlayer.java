package de.d2dev.heroquest.engine.sound;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.plugins.WAVLoader;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;

import de.d2dev.fourseasons.resource.JmeAssetLocatorAdapter;
import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.heroquest.engine.game.GameListener;
import de.d2dev.heroquest.engine.game.Hero;
import de.d2dev.heroquest.engine.game.Monster;
import de.d2dev.heroquest.engine.game.action.AttackAction;
import de.d2dev.heroquest.engine.game.action.MoveAction;

public class JmeSoundPlayer implements GameListener {
	
	private AudioRenderer audioRenderer;
	private AssetManager assetManager;

	private AudioNode stepSound;
	private long lastStep = 0;
	private long stepDelay = 200;
	
	private AudioNode monsterKillSound;
	
	
	public JmeSoundPlayer(ResourceLocator resourceFinder) {
		assetManager = JmeSystem.newAssetManager();
		assetManager.registerLoader( WAVLoader.class, "wav" ); 
		
		assetManager.registerLocator( "audioLocator", JmeAssetLocatorAdapter.class );
		JmeAssetLocatorAdapter.locators.put( "audioLocator", resourceFinder );
		
		
		audioRenderer = JmeSystem.newAudioRenderer( new AppSettings(true) );
		audioRenderer.initialize();
		
		stepSound = new AudioNode( assetManager, "step.wav" );
	}

	@Override
	public void onMoveAction(MoveAction action) {
		long time = System.currentTimeMillis();
		
		if ( time - lastStep > stepDelay ) {
			audioRenderer.playSourceInstance( stepSound );
			
			lastStep = time;
		}
	}

	@Override
	public void onAttackAction(AttackAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHeroAttacksMonster(Hero hero, Monster monster) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMonsterAttacksHero(Monster monster, Hero hero) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHeroKillsMonster(Hero hero, Monster monster) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMonsterKillsHero(Monster monster, Hero hero) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHeroHeroDies(Hero hero) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMonsterDies(Monster monster) {
		audioRenderer.playSourceInstance( monsterKillSound );
	}
}
