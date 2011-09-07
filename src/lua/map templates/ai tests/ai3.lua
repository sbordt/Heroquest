require 'bindings'
require 'map templates/map_creation'

-- Have 4 goblins in a row we want them to attack 2 heroes!
function createMap ()

	map = luajava.newInstance( MapClass, 10, 10 );
	
	unitFactory:createBarbarian( map:getField( 4, 6 ) );
	unitFactory:createDwarf( map:getField( 6, 6 ) );
	
	unitFactory:createGoblin( map:getField( 2, 1 ) );
	unitFactory:createGoblin( map:getField( 2, 2 ) );
	unitFactory:createGoblin( map:getField( 2, 3 ) );
	unitFactory:createGoblin( map:getField( 2, 4 ) );
	unitFactory:createGoblin( map:getField( 2, 5 ) );
	unitFactory:createGoblin( map:getField( 2, 6 ) );
	unitFactory:createGoblin( map:getField( 2, 7 ) );
	unitFactory:createGoblin( map:getField( 2, 8 ) );
	
	return map;
end






