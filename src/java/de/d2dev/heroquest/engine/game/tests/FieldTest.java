package de.d2dev.heroquest.engine.game.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.d2dev.fourseasons.gamestate.GameStateException;
import de.d2dev.heroquest.engine.game.Door;
import de.d2dev.heroquest.engine.game.Map;

public class FieldTest {
	
	Map map;
	
	@Before
	public void before() throws Exception {
		// simple map
		map = new Map( 10, 10 );
		
		map.getField( 1 , 1 ).setWall(true);
		map.getField( 1 , 2 ).setWall(true);
		map.getField( 1 , 3 ).setWall(true);
		map.getField( 1 , 4 ).setWall(true);
		
		map.getField( 2 , 1 ).setWall(true);
		map.getField( 3 , 1 ).setWall(true);
		map.getField( 4 , 1 ).setWall(true);
		
		map.getField( 4 , 2 ).setWall(true);
		map.getField( 4 , 3 ).setWall(true);
		map.getField( 4 , 4 ).setWall(true);
		
		map.getField( 2 , 4 ).setWall(true);
		map.getField( 3 , 4 ).setWall(true);
	}
	
	@After
	public void after() {
		
	}
	
	@Test(expected = GameStateException.class)
	public void invalidDoor() throws Exception {
		map.getField(0, 0).setDoor(true);
	}

	@Test
	public void testDoor() throws Exception {
		assertNull( map.getField(1, 2).getDoor() );
		
		map.getField(1, 2).setDoor(true);
		Door door = map.getField(1, 2).getDoor();
		
		assertNotNull( door );
		assertTrue( door.isClosed() );
		assertFalse( door.isOpen() );
		
		door.open();
		
		assertTrue( door.isOpen() );
		assertFalse( door.isClosed() );		
	}

	@Test
	public void testRoom() throws Exception {
		for (int i=0; i<map.getWidth(); i++)
			for (int j=0; j<map.getHeight(); j++)
				assertNull( map.getField(i, j).getRoom() );
	}	
}
