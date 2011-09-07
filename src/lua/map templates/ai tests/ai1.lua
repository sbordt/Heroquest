require 'bindings'
require 'map templates/map_creation'

-- Have 4 gargoyles and 1 goblin in a row we want the gargoyles and not the goblin to attack!
function createMap ()

	map = luajava.newInstance( MapClass, 10, 10 );
	
	unitFactory:createBarbarian( map:getField( 5, 3 ) );
	
	unitFactory:createGargoyle( map:getField( 2, 1 ) );
	unitFactory:createGargoyle( map:getField( 2, 2 ) );
	unitFactory:createGoblin( map:getField( 2, 3 ) );
	unitFactory:createGargoyle( map:getField( 2, 4 ) );
	unitFactory:createGargoyle( map:getField( 2, 5 ) );
	
	return map;
end






