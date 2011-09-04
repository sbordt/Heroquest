require 'bindings'
require 'map templates/map_creation'
require 'map templates/classical'

function createMap ()
	map = create_classical_map ();
	
	map:getField( 1, 3 ):setDoor( true );
	map:getField( 3, 1 ):setDoor( true );
	
	return map;
end






