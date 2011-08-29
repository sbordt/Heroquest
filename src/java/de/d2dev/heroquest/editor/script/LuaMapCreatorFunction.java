package de.d2dev.heroquest.editor.script;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import de.d2dev.fourseasons.script.lua.LuaScriptFunction;
import de.d2dev.heroquest.engine.game.Map;

public class LuaMapCreatorFunction extends LuaScriptFunction implements MapCreatorFunction {
	
	public final static String LUA_FUNCTION_NAME = "createMap";
	
	private LuaFunction function;
	
	public LuaMapCreatorFunction(LuaFunction function) {
		this.function = function;
	}

	@Override
	public Map createMap() {
		LuaValue result = this.function.call();
		
		return (Map) result.touserdata();
	}

}
