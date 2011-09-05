require 'bindings'
require 'map templates/map_creation'
require 'map templates/classical'

function createMap ()
	map = create_classical_map ();
	
	map:getField( 1, 3 ):setDoor( true );
	map:getField( 1, 8 ):setDoor( true );
	map:getField( 1, 16 ):setDoor( true );
	
	map:getField( 3, 5 ):setDoor( true );
	map:getField( 3, 18 ):setDoor( true );
	map:getField( 3, 23 ):setDoor( true );
	
	map:getField( 4, 11 ):setDoor( true );
	map:getField( 4, 13 ):setDoor( true );
	
	map:getField( 6, 3 ):setDoor( true );
	map:getField( 6, 9 ):setDoor( true );
	map:getField( 6, 15 ):setDoor( true );
	
	add_texture_overlay( map, 24, 2, 4, 4, "overlays/turning_room.png", 0 );
	add_texture_overlay( map, 15, 10, 6, 5, "overlays/throne_room.png", 1 );
	add_texture_overlay( map, 8, 2, 3, 2, "overlays/shadow_hiding_1.png", 0 );
	add_texture_overlay( map, 8, 7, 5, 4, "overlays/armory.png", 0 );
	
	return map;
end






