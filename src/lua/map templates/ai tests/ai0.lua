require 'bindings'
require 'map templates/map_creation'

-- Have 4 goblins in a row we want them to attack the hero!
function createMap ()

	map = luajava.newInstance( MapClass, 10, 10 );
	
	unitFactory:createBarbarian( map:getField( 4, 6 ) );
	
	unitFactory:createGoblin( map:getField( 2, 1 ) );
	unitFactory:createGoblin( map:getField( 2, 2 ) );
	unitFactory:createGoblin( map:getField( 2, 3 ) );
	unitFactory:createGoblin( map:getField( 2, 4 ) );
	
	return map;
end






