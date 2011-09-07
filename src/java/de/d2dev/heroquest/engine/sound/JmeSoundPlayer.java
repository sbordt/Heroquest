package de.d2dev.heroquest.engine.sound;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.plugins.OGGLoader;
import com.jme3.audio.plugins.WAVLoader;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;

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
		
	//private AudioNode backgroundMusic;
	private AudioNode monsterKillSound;
	
	
	//@SuppressWarnings("deprecation")	// need to call deprecated to make ogg working
	public JmeSoundPlayer(AssetManager assetManager) {
		this.assetManager = assetManager;
		
		assetManager.registerLoader( WAVLoader.class, "wav" ); 
		assetManager.registerLoader( OGGLoader.class, "ogg" ); 
	
		audioRenderer = JmeSystem.newAudioRenderer( new AppSettings(true) );
		audioRenderer.initialize();
		
		stepSound = new AudioNode( this.assetManager, "step.wav" );
		stepSound.setVolume( 0.1f );

		monsterKillSound = new AudioNode( this.assetManager, "monster_kill.wav" );
		monsterKillSound.setVolume( 0.1f );
		
		//backgroundMusic = new AudioNode( this.audioRenderer, this.assetManager, "music/HeroeQuest.ogg", true );
		//backgroundMusic.setLooping(true);
		//backgroundMusic.setVolume( 0.5f );
		//this.audioRenderer.playSource( this.backgroundMusic );
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
