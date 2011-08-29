
-- Set a texture to all fields in a horizontal line
function horizontal_texture (map, x, y, length, texture)
	for i=x,x+length-1 do
		map:getField( i, y ):getTexture():setName( texture ); 
	end
end

