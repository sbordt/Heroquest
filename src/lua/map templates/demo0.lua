require 'bindings'
require 'map templates/map_creation'
require 'map templates/classical'

function createMap ()

	map = luajava.newInstance( MapClass, 25, 16 );
	
	add_texture_overlay( map, 0, 0, 2, 2, "overlays/stairs.png", 0 );
	
	room( map, 2, 1, 6, 6 , "fields/yellow/yellow.jpg" );
	add_texture_overlay( map, 3, 2, 4, 4, "overlays/turning_room.png", 0 );
	map:getField( 2, 4 ):setDoor( true );
	
	room( map, 1, 8, 7, 6 , "fields/yellow/yellow.jpg" );

	room( map, 7, 1, 6, 6 , "fields/yellow/yellow.jpg" );
	add_texture_overlay( map, 8, 2, 4, 4, "overlays/treasury.png", 0 );
	map:getField( 7, 3 ):setDoor( true );
	
	room( map, 12, 2, 6, 6 , "fields/yellow/yellow.jpg" );
	add_texture_overlay( map, 13, 3, 4, 4, "overlays/wall_of_light.png", 0 );
	
	room( map, 9, 9, 7, 6 , "fields/yellow/yellow.jpg" );
	add_texture_overlay( map, 10, 10, 5, 4, "overlays/armory.png", 0 );
	
	room( map, 17, 9, 8, 7 , "fields/yellow/yellow.jpg" );
	add_texture_overlay( map, 18, 10, 6, 5, "overlays/throne_room.png", 1 );
	
	add_texture_overlay( map, 1, 14, 3, 2, "overlays/shadow_hiding_1.png", 0 );
	add_texture_overlay( map, 20, 3, 2, 2, "overlays/shadow_hiding_2.png", 0 );
	
	map:getField( 14, 2 ):setDoor( true );
	map:getField( 14, 9 ):setDoor( true );
	map:getField( 7, 11 ):setDoor( true );
	map:getField( 9, 11 ):setDoor( true );
	map:getField( 17, 12 ):setDoor( true );
	map:getField( 21, 9 ):setDoor( true );
	
	return map;
end






