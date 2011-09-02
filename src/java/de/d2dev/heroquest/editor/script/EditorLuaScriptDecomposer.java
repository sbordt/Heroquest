package de.d2dev.heroquest.editor.script;

import java.util.List;
import java.util.Vector;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import de.d2dev.fourseasons.script.lua.LuaScriptDecomposer;
import de.d2dev.fourseasons.script.lua.LuaScriptFunction;

public class EditorLuaScriptDecomposer extends LuaScriptDecomposer {

	@Override
	public List<LuaScriptFunction> decompose(LuaFunction script) {		
		Vector<LuaScriptFunction> functions = new Vector<LuaScriptFunction>();
		
		LuaValue env = script.getfenv();
				
		try {
			// map creation functions
			LuaValue createMapFnc = env.get( "createMap" );
			
			if ( !createMapFnc.isnil() ) {
				functions.add( new LuaMapCreatorFunction( (LuaFunction) createMapFnc ) );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return functions;
	}
}
