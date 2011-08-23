package de.d2dev.heroquest.engine.files.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import de.d2dev.heroquest.engine.files.HqRessourceFile;

public class HqRessourceFileTest {

	@Test
	public void test() throws Exception {
		HqRessourceFile.createHqRessourceFile( "C:/Users/Batti/Desktop/tests/ressources.zip" );
	}

}
