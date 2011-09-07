require 'bindings'
require 'map templates/map_creation'

-- Have and 4 goblins blocking a hero and an orc next to them - we want the orc to attack!
function createMap ()

	map = luajava.newInstance( MapClass, 10, 10 );
	
	unitFactory:createBarbarian( map:getField( 5, 3 ) );
	
	unitFactory:createGoblin( map:getField( 4, 3 ) );
	unitFactory:createGoblin( map:getField( 6, 3 ) );
	unitFactory:createGoblin( map:getField( 5, 2 ) );
	unitFactory:createGoblin( map:getField( 5, 4 ) );
	
	unitFactory:createOrc( map:getField( 0, 0 ) );
	
	return map;
end






