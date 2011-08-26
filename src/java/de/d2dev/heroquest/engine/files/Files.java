package de.d2dev.heroquest.engine.files;

import de.schlichtherle.truezip.file.TArchiveDetector;
import de.schlichtherle.truezip.file.TConfig;
import de.schlichtherle.truezip.fs.archive.zip.JarDriver;
import de.schlichtherle.truezip.socket.sl.IOPoolLocator;

public class Files {
	
	/**
	 * Register our custom file formats as archive files with the Truezip {@code TConfig} driver.
	 * To be called once at application setup. File types to be registered:<br>
	 * <br>
	 * *.hqmap - Jar-File
	 * @author Sebastian Bordt 
	 */
	public static void registerFileFormats() {
		 TConfig config = TConfig.get();
		 TArchiveDetector detector = config.getArchiveDetector();
		 
		 detector = new TArchiveDetector( detector, HqRessourceFile.EXTENSION, new JarDriver(IOPoolLocator.SINGLETON) );
		 detector = new TArchiveDetector( detector, HqMapFile.EXTENSION, new JarDriver(IOPoolLocator.SINGLETON) );
		 
		 config.setArchiveDetector(detector);
	}
}
