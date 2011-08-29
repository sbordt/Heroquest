package de.d2dev.heroquest.engine.files;

import java.io.IOException;

import nu.xom.Document;
import nu.xom.ParsingException;

import de.d2dev.fourseasons.VersionNumber;
import de.d2dev.fourseasons.files.AbstractContainerFile;
import de.d2dev.fourseasons.files.FileUtil;
import de.d2dev.fourseasons.files.MagicStringException;
import de.d2dev.heroquest.engine.game.Map;
import de.schlichtherle.truezip.file.TFile;

public class HqMapFile extends AbstractContainerFile {
	
	public static final String EXTENSION = "hqmap";
	public static final String MAGIC_STRING = "HEROQUEST_MAP";
	
	public static final VersionNumber VERSION = new VersionNumber(1, 0); 
	
	public static final String MAP_FILE_NAME = "map.xml";
	
	/**
	 * Create a new map file.
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws MagicStringException
	 * @throws ParsingException
	 */
	public static HqMapFile createHqMapFile(String path, Map map) throws IOException, MagicStringException, ParsingException {
		AbstractContainerFile.createEmptyContainer( path, MAGIC_STRING, VERSION );	
		
		FileUtil.writeXMLToFile( new TFile( path + "/" + MAP_FILE_NAME), new Document( map.toXML() ) );	// the map
		
		return new HqMapFile( path );
	}
	
	public Document map;
	
	/**
	 * Load an existing map.
	 * @param path
	 * @throws ParsingException 
	 * @throws MagicStringException 
	 * @throws Exception
	 */
	public HqMapFile(String path) throws IOException, MagicStringException, ParsingException {
		super( path );
		
		// parse the map
		this.map = FileUtil.readXMLFromFile( new TFile( this.file.getAbsolutePath() + "/" + MAP_FILE_NAME ) );
	}
	
	/**
	 * Save the contents of the file to disk.
	 * @throws IOException 
	 */
	public void save() throws IOException {
		// save the map
		FileUtil.writeXMLToFile( new TFile( this.file.getAbsolutePath() + "/" + MAP_FILE_NAME), this.map );
	}
	
	@Override
	public String getMagicString() {
		return MAGIC_STRING;
	}
}
