package de.d2dev.heroquest.engine.files;

import java.io.IOException;

import nu.xom.ParsingException;

import de.d2dev.fourseasons.MagicStringException;
import de.d2dev.fourseasons.VersionNumber;
import de.d2dev.fourseasons.files.AbstractContainerFile;

public class HqMapFile extends AbstractContainerFile {
	
	public static final String EXTENSION = "hqmap";
	public static final String MAGIC_STRING = "HEROQUEST_MAP";
	
	public static final VersionNumber VERSION = new VersionNumber(1, 0); 
	
	/**
	 * Create a new map file.
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws MagicStringException
	 * @throws ParsingException
	 */
	public static HqMapFile createHqMapFile(String path) throws IOException, MagicStringException, ParsingException {
		AbstractContainerFile.createEmptyContainer( path, MAGIC_STRING, VERSION );
		return new HqMapFile( path );
	}
	
	/**
	 * Load an existing map.
	 * @param path
	 * @throws ParsingException 
	 * @throws MagicStringException 
	 * @throws Exception
	 */
	public HqMapFile(String path) throws IOException, MagicStringException, ParsingException {
		super( path );
	}
	

	@Override
	public String getMagicString() {
		return MAGIC_STRING;
	}
}
