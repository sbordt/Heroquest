package de.d2dev.heroquest.engine.rendering;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import de.d2dev.fourseasons.files.ImageUtil;
import de.d2dev.fourseasons.resource.Resource;
import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.TextureResource;

import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.Map;

/**
 * This class creates textures for walls and stores them in
 * a temporary directory. Also see US Patent 10.000 ("Automate wall textures and 
 * temporary files in the HeroQuest technology").
 * @author Sebastian Bordt
 *
 */
public class WallTextureCreator {
	
	public int textureSize = 40;
	
	File tmpFolder;
	
	Map map;
	
	ResourceLocator resourceFinder;
	Resource outerTexture;
	
	public WallTextureCreator(Map map, ResourceLocator resourceFinder, Resource outerTexture) {
		this.map = map;
		this.resourceFinder = resourceFinder;
		this.outerTexture = outerTexture;
		
		// temp folder to store our images
		this.tmpFolder = Files.createTempDir();	
		this.tmpFolder.deleteOnExit();	
	}
	
	public Resource createWallTexture(Field field) {
		Preconditions.checkArgument( field.isWall() );
		
		int x = field.getX();
		int y = field.getY();
		
		// prepare the image
		BufferedImage img = new BufferedImage( this.textureSize, this.textureSize,  BufferedImage.TYPE_INT_ARGB );
		Graphics2D g = img.createGraphics();
		
		g.setColor( Color.WHITE );
		g.fillRect( 0, 0, img.getWidth(), img.getHeight() );
		
		// field does not touch the maps borders - 11 cases! lot's of code but does it and errors are easy to find
		if ( x != 0 && x != map.getWidth() -1 && y != 0 && y != map.getHeight() -1 ) {
			Field upperField = map.getField( x, y-1 );
			Field upperLeftField = map.getField( x-1, y-1 );
			Field upperRightField = map.getField( x+1, y-1 );
			Field lowerField = map.getField( x, y+1 );
			Field lowerLeftField = map.getField( x-1, y+1 );
			Field lowerRightField = map.getField( x+1, y+1 );
			Field leftField = map.getField( x-1, y );
			Field rightField = map.getField( x+1, y );

			// horizontal wall
			if ( !upperField.isWall() && !lowerField.isWall() && leftField.isWall() && rightField.isWall() ) {
				this.copyPasteUpper(g, img, upperField);
				this.copyPasteLower(g, img, lowerField);
			}
			
			// horizontal and bottom
			else if ( !upperField.isWall() && lowerField.isWall() && leftField.isWall() && rightField.isWall() ) {
				this.copyPasteLowerLeft(g, img, lowerLeftField);
				this.copyPasteLowerRight(g, img, lowerRightField);
				
				this.copyPasteUpper(g, img, upperField);
			}
			
			// horizontal and top
			else if ( upperField.isWall() && !lowerField.isWall() && leftField.isWall() && rightField.isWall() ) {		
				this.copyPasteUpperLeft(g, img, upperLeftField);
				this.copyPasteUpperRight(g, img, upperRightField);
				
				this.copyPasteLower(g, img, lowerField);
			}	
			
			// vertical wall
			else if ( upperField.isWall() && lowerField.isWall() && !leftField.isWall() && !rightField.isWall() ) { 
				this.copyPasteLeft(g, img, leftField);
				this.copyPasteRight(g, img, rightField);
			}
			
			// vertical and left
			else if ( upperField.isWall() && lowerField.isWall() && leftField.isWall() && !rightField.isWall() ) { 
				this.copyPasteUpperLeft(g, img, upperLeftField);
				this.copyPasteLowerLeft(g, img, lowerLeftField);
				
				this.copyPasteRight(g, img, rightField);
			}
			
			// vertical and right
			else if ( upperField.isWall() && lowerField.isWall() && !leftField.isWall() && rightField.isWall() ) { 
				this.copyPasteUpperRight(g, img, upperRightField);
				this.copyPasteLowerRight(g, img, lowerRightField);
				
				this.copyPasteLeft(g, img, leftField);
			}
			
			// lower left edge
			else if ( !upperField.isWall() && lowerField.isWall() && leftField.isWall() && !rightField.isWall() ) { 
				this.copyPasteLowerLeft(g, img, lowerLeftField);
				
				this.copyPasteUpper(g, img, upperField);
				this.copyPasteImg( g, img, this.loadTexture( upperField.getTexture() ), 0.6, 0.4, 1, 1 );
			}
			
			// lower right edge
			else if ( !upperField.isWall() && lowerField.isWall() && !leftField.isWall() && rightField.isWall() ) { 
				this.copyPasteLowerRight(g, img, lowerRightField);
				
				this.copyPasteUpper(g, img, upperField);
				this.copyPasteImg( g, img, this.loadTexture( upperField.getTexture() ), 0, 0.4, 0.4, 1 );
			}
			
			// upper left edge
			else if ( upperField.isWall() && !lowerField.isWall() && leftField.isWall() && !rightField.isWall() ) { 
				this.copyPasteUpperLeft(g, img, upperLeftField);
				
				this.copyPasteLower(g, img, lowerField);
				this.copyPasteImg( g, img, this.loadTexture( lowerField.getTexture() ), 0.6, 0, 1, 0.6 );
			}
			
			// upper right edge
			else if ( upperField.isWall() && !lowerField.isWall() && !leftField.isWall() && rightField.isWall() ) { 
				this.copyPasteUpperRight(g, img, upperRightField);
				
				this.copyPasteLower(g, img, lowerField);
				this.copyPasteImg( g, img, this.loadTexture( lowerField.getTexture() ), 0, 0, 0.4, 0.6 );
			}
			
			// cross
			else if ( upperField.isWall() && lowerField.isWall() && leftField.isWall() && rightField.isWall() ) { 
				this.copyPasteUpperRight(g, img, upperRightField);
				this.copyPasteUpperLeft(g, img, upperLeftField);
				this.copyPasteLowerLeft(g, img, lowerLeftField);
				this.copyPasteLowerRight(g, img, lowerRightField);
			}
		}
		
		// write the image to a temporary file - done
		try {
			File tmpFile = File.createTempFile( "uspatent", ".jpg", this.tmpFolder );
			ImageUtil.writeJpg(img,  tmpFile);
			
			return TextureResource.createTextureResource( tmpFile.getAbsolutePath() );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return TextureResource.createTextureResource( "error.jpg" );	// indicate error
	}
	
	private BufferedImage loadTexture(Resource texture) {
		BufferedImage img;
		
		try {
			return TextureResource.load( this.resourceFinder.getAbsoluteLocation( texture ) );
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		// provide magenta picture to indicate the error
		img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB );
		Graphics2D graphics = img.createGraphics();

		graphics.setColor( Color.MAGENTA );
		graphics.fillRect( 0, 0, img.getWidth(), img.getHeight() );

		return img;
	}
	
	/*
	 * Helpers to copy&paste common image parts
	 */
	public void copyPasteUpper(Graphics2D g, BufferedImage dest, Field field) {
		this.copyPasteImg( g, dest, this.loadTexture( field.getTexture() ), 0, 0, 1, 0.4 );
	}
	
	public void copyPasteUpperLeft(Graphics2D g, BufferedImage dest, Field field) {
		this.copyPasteImg( g, dest, this.loadTexture( field.getTexture() ), 0, 0, 0.4, 0.4 );
	}
	
	public void copyPasteUpperRight(Graphics2D g, BufferedImage dest, Field field) {
		this.copyPasteImg( g, dest, this.loadTexture( field.getTexture() ), 0.6, 0, 1, 0.4 );
	}
	
	public void copyPasteLower(Graphics2D g, BufferedImage dest, Field field) {
		this.copyPasteImg( g, dest, this.loadTexture( field.getTexture() ), 0, 0.6, 1, 1 );
	}
	
	public void copyPasteLowerLeft(Graphics2D g, BufferedImage dest, Field field) {
		this.copyPasteImg( g, dest, this.loadTexture( field.getTexture() ), 0, 0.6, 0.4, 1 );
	}
	
	public void copyPasteLowerRight(Graphics2D g, BufferedImage dest, Field field) {
		this.copyPasteImg( g, dest, this.loadTexture( field.getTexture() ), 0.6, 0.6, 1, 1 );
	}
	
	public void copyPasteLeft(Graphics2D g, BufferedImage dest, Field field) {
		this.copyPasteImg( g, dest, this.loadTexture( field.getTexture() ), 0, 0, 0.4, 1 );
	}
	
	public void copyPasteRight(Graphics2D g, BufferedImage dest, Field field) {
		this.copyPasteImg( g, dest, this.loadTexture( field.getTexture() ), 0.6, 0, 1, 1 );
	}
	
	/**
	 * Handy helper. Copy&paste owns.
	 * @param g
	 * @param dest
	 * @param src
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 */
	private void copyPasteImg(Graphics2D g, BufferedImage dest, BufferedImage src, double left, double top, double width, double height) {
		g.drawImage( src, 
				(int) (dest.getWidth() * left), 
				(int) (dest.getHeight() * top), 
				(int) (dest.getHeight() * width),  
				(int) (dest.getHeight() * height), 
				(int) (src.getWidth() * left), 
				(int) (src.getHeight() * top), 
				(int) (src.getHeight() * width),  
				(int) (src.getHeight() * height), 
				null );
	}
}
