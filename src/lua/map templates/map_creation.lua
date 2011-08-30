
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

-- Create a simple room
function room(map, x, y, width, height, texture)
	rect_wall(map, x, y, width, height);
	rect_texture_fill(map, x+1, y+1, width-2, height-2, texture);
end
