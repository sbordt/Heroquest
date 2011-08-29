package de.d2dev.heroquest.engine.game.tests;

import static org.junit.Assert.*;

import nu.xom.Document;

import org.junit.Test;

import de.d2dev.fourseasons.files.FileUtil;
import de.d2dev.heroquest.engine.game.Map;

public class MapTest {

	@Test
	public void save_load() throws Exception {
		Map map = new Map(10, 10);
		
		Document xml = new Document( map.toXML() );
		
		FileUtil.writeXMLToFile( "C:/Users/Batti/Desktop/tests/map.xml", xml );
		
		xml = FileUtil.readXMLFromFile( "C:/Users/Batti/Desktop/tests/map.xml" );
		
		map = new Map( xml.getRootElement() );
		
		assertEquals( 10, map.getWidth() );
		assertEquals( 10, map.getHeight() );
		
		assertEquals( 10, map.getFields().length );
		
		for (int i=0; i<10; i++)
			assertEquals( 10, map.getFields()[i].length );
	}

}
