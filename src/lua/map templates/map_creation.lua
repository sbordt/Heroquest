
unitFactory = luajava.newInstance( UnitFactoryClass );

-- Set a texture to all fields in a horizontal line
function horizontal_texture (map, x, y, length, texture)
	for i=x,x+length-1 do
		map:getField( i, y ):getTexture():setName( texture ); 
	end
end

-- Set a texture to all fields in a vertical line
function vertical_texture (map, x, y, length, texture)
	for i=y,y+length-1 do
		map:getField( x, i ):getTexture():setName( texture ); 
	end
end

-- Fill a rectangle with a texture
function rect_texture_fill(map, x, y, width, height, texture)
	for i=y,y+height-1 do
		horizontal_texture(map, x, i, width, texture);
	end
end

-- Create a horizontal wall
function horizontal_wall(map, x, y, length)
	for i=x,x+length-1 do
		map:getField( i, y ):setWall( true ); 
	end
end

-- Create a vertical wall
function vertical_wall(map, x, y, length)
	for i=y,y+length-1 do
		map:getField( x, i ):setWall( true ); 
	end
end

-- Create a rectangular wall
function rect_wall(map, x, y, width, height)
	horizontal_wall(map, x, y, width);
	horizontal_wall(map, x, y+height-1, width);
	vertical_wall(map, x, y, height);
	vertical_wall(map, x+width-1, y, height);
end

-- Simple algorithm to automatically detect rooms.
-- From a given starting position the algorithm recursively adds all fields next to it to a new room. 
-- The algorithm won't continue if it encouters a wall.
function auto_detect_room(map, x, y)
	return auto_detect_room_impl( map, map:addRoom(), map:getField(x, y) );

end

function auto_detect_room_impl(map, r, f)	
	-- termination condition
	if f:isWall() or f:belongsToRoom() then
		return;
	end
	
	-- add the field to the room
	r:addField( f );
	
	-- recursively look at the neighbour fields
	l = f:getNeighbours();
	
	for i=0,l:size()-1 do
		auto_detect_room_impl( map, r, l:get( i ) );
	end
end

-- Create a simple room
function room(map, x, y, width, height, texture)
	rect_wall(map, x, y, width, height);
	rect_texture_fill(map, x+1, y+1, width-2, height-2, texture);
	
	auto_detect_room( map, x+1, y+1 );
end

-- Create a simple room 
-- turn 0, 1, 2, 3 <=> normal, 90, 180, 270 
function add_texture_overlay(map, x, y, width, height, texture, turn)
	overlay = map:addTextureOverlay();
	
	overlay:setX( x );
	overlay:setY( y );
	overlay:setWidth( width );
	overlay:setHeight( height );
	overlay:getTexture():setName( texture );
	
	if turn == 0 then
		overlay:setTurnToNormal();
	elseif turn == 1 then
		overlay:setTurnTo90Degree();
	elseif turn == 2 then
		overlay:setTurnTo180Degree();
	elseif turn == 3 then
		overlay:setTurnTo270Degree();		
	end
end
