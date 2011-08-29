require 'map templates/map_creation'

function createMap ()
	map = luajava.newInstance( "de.d2dev.heroquest.engine.gamestate.Map", 15, 15 );
	
	for i=0,13 do
		map:getField( i, i ):getTexture():setName( "fields/yellow/yellow.jpg" ); 
	end
	
	horizontal_texture( map, 6, 3, 4, "fields/yellow/yellow.jpg" );
	
	return map;
end




