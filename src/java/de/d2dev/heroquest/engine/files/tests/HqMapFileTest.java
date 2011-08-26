package de.d2dev.heroquest.engine.files.tests;

import org.junit.BeforeClass;
import org.junit.Test;

import de.d2dev.heroquest.engine.files.Files;
import de.d2dev.heroquest.engine.files.HqMapFile;

public class HqMapFileTest {
	
	@BeforeClass
	public static void register() {
		Files.registerFileFormats();
	}

	@Test
	public void create() throws Exception {
		HqMapFile.createHqMapFile( "C:/Users/Batti/Desktop/tests/mymap.hqmap" );
	}
}
