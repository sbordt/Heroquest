require 'bindings'
require 'map templates/map_creation'

-- Have and 3 goblins attacking a hero and an orc blocked by two other goblins - we want the orc to attack!
function createMap ()

	map = luajava.newInstance( MapClass, 10, 10 );
	
	unitFactory:createBarbarian( map:getField( 3, 3 ) );
	
	unitFactory:createGoblin( map:getField( 4, 3 ) );
	unitFactory:createGoblin( map:getField( 3, 2 ) );
	unitFactory:createGoblin( map:getField( 3, 4 ) );
	
	unitFactory:createOrc( map:getField( 0, 0 ) );
	unitFactory:createGoblin( map:getField( 0, 1 ) );
	unitFactory:createGoblin( map:getField( 1, 0 ) );
	
	return map;
end






