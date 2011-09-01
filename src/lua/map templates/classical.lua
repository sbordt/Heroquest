require 'bindings'
require 'map templates/map_creation'

function createMap ()
	map = luajava.newInstance( MapClass, 35, 25 );

	-- upper left
	room( map, 1, 1, 6, 5, "fields/yellow/yellow.jpg" );
	room( map, 1, 5, 6, 7, "fields/yellow/yellow.jpg" );
	room( map, 6, 1, 7, 5, "fields/yellow/yellow.jpg" );
	room( map, 6, 5, 7, 7, "fields/yellow/yellow.jpg" );
	room( map, 12, 1, 5, 7, "fields/yellow/yellow.jpg" );
	
	-- upper right
	room( map, 19, 1, 5, 7, "fields/yellow/yellow.jpg" );
	room( map, 23, 1, 6, 6, "fields/yellow/yellow.jpg" );
	room( map, 28, 1, 6, 6, "fields/yellow/yellow.jpg" );
	room( map, 23, 6, 6, 6, "fields/yellow/yellow.jpg" );
	room( map, 28, 6, 6, 6, "fields/yellow/yellow.jpg" );
	
	-- room in the middle
	room( map, 14, 9, 8, 7, "fields/yellow/yellow.jpg" );
	
	-- lower left
	room( map, 1, 13, 6, 6, "fields/yellow/yellow.jpg" );
	room( map, 1, 18, 6, 6, "fields/yellow/yellow.jpg" );
	room( map, 6, 13, 4, 5, "fields/yellow/yellow.jpg" );
	room( map, 9, 13, 4, 5, "fields/yellow/yellow.jpg" );
	room( map, 6, 17, 7, 7, "fields/yellow/yellow.jpg" );
	room( map, 12, 17, 5, 7, "fields/yellow/yellow.jpg" );
	
	-- lower right
	room( map, 19, 17, 6, 7, "fields/yellow/yellow.jpg" );
	room( map, 24, 18, 5, 6, "fields/yellow/yellow.jpg" );
	room( map, 28, 18, 6, 6, "fields/yellow/yellow.jpg" );
	room( map, 28, 13, 6, 6, "fields/yellow/yellow.jpg" );
	
	horizontal_wall( map, 23, 13, 5 );	-- this room is not rectangular :-(
	vertical_wall( map, 23, 13, 4 );
	horizontal_texture( map, 24, 14, 4, "fields/yellow/yellow.jpg" );
	horizontal_texture( map, 24, 15, 4, "fields/yellow/yellow.jpg"  );
	horizontal_texture( map, 24, 16, 4, "fields/yellow/yellow.jpg"  );
	horizontal_texture( map, 25, 17, 3, "fields/yellow/yellow.jpg"  );
	
	return map;
end




