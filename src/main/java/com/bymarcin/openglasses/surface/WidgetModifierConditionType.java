package com.bymarcin.openglasses.surface;

public class WidgetModifierConditionType {
	public static final short
			IS_WEATHER_RAIN = 1,
			IS_WEATHER_CLEAR = 2,
			IS_SWIMMING = 3,
			IS_NOT_SWIMMING = 4,
			IS_SNEAKING = 5,
			IS_NOT_SNEAKING	= 6,
			OVERLAY_ACTIVE = 7,
			OVERLAY_INACTIVE = 8,
			IS_LIGHTLEVEL_MIN_0 = 9,
			IS_LIGHTLEVEL_MIN_1 = 10,
			IS_LIGHTLEVEL_MIN_2 = 11,
			IS_LIGHTLEVEL_MIN_3 = 12,
			IS_LIGHTLEVEL_MIN_4 = 13,
			IS_LIGHTLEVEL_MIN_5 = 14,
			IS_LIGHTLEVEL_MIN_6 = 15,
			IS_LIGHTLEVEL_MIN_7 = 16,
			IS_LIGHTLEVEL_MIN_8 = 17,
			IS_LIGHTLEVEL_MIN_9 = 18,
			IS_LIGHTLEVEL_MIN_10 = 19,
			IS_LIGHTLEVEL_MIN_11 = 20,
			IS_LIGHTLEVEL_MIN_12 = 21,
			IS_LIGHTLEVEL_MIN_13 = 22,
			IS_LIGHTLEVEL_MIN_14 = 23,
			IS_LIGHTLEVEL_MIN_15 = 24,
			IS_LIGHTLEVEL_MAX_0 = 25,
			IS_LIGHTLEVEL_MAX_1 = 26,
			IS_LIGHTLEVEL_MAX_2 = 27,
			IS_LIGHTLEVEL_MAX_3 = 28,
			IS_LIGHTLEVEL_MAX_4 = 29,
			IS_LIGHTLEVEL_MAX_5 = 30,
			IS_LIGHTLEVEL_MAX_6 = 31,
			IS_LIGHTLEVEL_MAX_7 = 32,
			IS_LIGHTLEVEL_MAX_8 = 33,
			IS_LIGHTLEVEL_MAX_9 = 34,
			IS_LIGHTLEVEL_MAX_10 = 35,
			IS_LIGHTLEVEL_MAX_11 = 36,
			IS_LIGHTLEVEL_MAX_12 = 37,
			IS_LIGHTLEVEL_MAX_13 = 38,
			IS_LIGHTLEVEL_MAX_14 = 39,
			IS_LIGHTLEVEL_MAX_15 = 40,
			IS_FOCUSED_ENTITY = 41,
			IS_FOCUSED_BLOCK = 42,
			IS_LIVING = 43,
			IS_HOSTILE = 44,
			IS_NEUTRAL = 45,
			IS_PLAYER = 46,
			IS_ITEM = 47;


	public static String getName(short v){
		switch(v){
			case 1: return "IS_WEATHER_RAIN";
			case 2: return "IS_WEATHER_CLEAR";
			case 3: return "IS_SWIMMING";
			case 4: return "IS_NOT_SWIMMING";
			case 5: return "IS_SNEAKING";
			case 6: return "IS_NOT_SNEAKING";
			case 7: return "OVERLAY_ACTIVE";
			case 8: return "OVERLAY_INACTIVE";
			case 9: return "IS_LIGHTLEVEL_MIN_0";
			case 10: return "IS_LIGHTLEVEL_MIN_1";
			case 11: return "IS_LIGHTLEVEL_MIN_2";
			case 12: return "IS_LIGHTLEVEL_MIN_3";
			case 13: return "IS_LIGHTLEVEL_MIN_4";
			case 14: return "IS_LIGHTLEVEL_MIN_5";
			case 15: return "IS_LIGHTLEVEL_MIN_6";
			case 16: return "IS_LIGHTLEVEL_MIN_7";
			case 17: return "IS_LIGHTLEVEL_MIN_8";
			case 18: return "IS_LIGHTLEVEL_MIN_9";
			case 19: return "IS_LIGHTLEVEL_MIN_10";
			case 20: return "IS_LIGHTLEVEL_MIN_11";
			case 21: return "IS_LIGHTLEVEL_MIN_12";
			case 22: return "IS_LIGHTLEVEL_MIN_13";
			case 23: return "IS_LIGHTLEVEL_MIN_14";
			case 24: return "IS_LIGHTLEVEL_MIN_15";
			case 25: return "IS_LIGHTLEVEL_MAX_0";
			case 26: return "IS_LIGHTLEVEL_MAX_1";
			case 27: return "IS_LIGHTLEVEL_MAX_2";
			case 28: return "IS_LIGHTLEVEL_MAX_3";
			case 29: return "IS_LIGHTLEVEL_MAX_4";
			case 30: return "IS_LIGHTLEVEL_MAX_5";
			case 31: return "IS_LIGHTLEVEL_MAX_6";
			case 32: return "IS_LIGHTLEVEL_MAX_7";
			case 33: return "IS_LIGHTLEVEL_MAX_8";
			case 34: return "IS_LIGHTLEVEL_MAX_9";
			case 35: return "IS_LIGHTLEVEL_MAX_10";
			case 36: return "IS_LIGHTLEVEL_MAX_11";
			case 37: return "IS_LIGHTLEVEL_MAX_12";
			case 38: return "IS_LIGHTLEVEL_MAX_13";
			case 39: return "IS_LIGHTLEVEL_MAX_14";
			case 40: return "IS_LIGHTLEVEL_MAX_15";
			case 41: return "IS_FOCUSED_ENTITY";
			case 42: return "IS_FOCUSED_BLOCK";
			case 43: return "IS_LIVING";
			case 44: return "IS_HOSTILE";
			case 45: return "IS_NEUTRAL";
			case 46: return "IS_PLAYER";
			case 47: return "IS_ITEM";
		}
		return "UNKNOWN";
	}

	public static short getIndex(String name){
		switch(name){
			case "IS_WEATHER_RAIN": return 1;
			case "IS_WEATHER_CLEAR": return 2;
			case "IS_SWIMMING": return 3;
			case "IS_NOT_SWIMMING": return 4;
			case "IS_SNEAKING": return 5;
			case "IS_NOT_SNEAKING": return 6;
			case "OVERLAY_ACTIVE": return 7;
			case "OVERLAY_INACTIVE": return 8;
			case "IS_LIGHTLEVEL_MIN_0": return 9;
			case "IS_LIGHTLEVEL_MIN_1": return 10;
			case "IS_LIGHTLEVEL_MIN_2": return 11;
			case "IS_LIGHTLEVEL_MIN_3": return 12;
			case "IS_LIGHTLEVEL_MIN_4": return 13;
			case "IS_LIGHTLEVEL_MIN_5": return 14;
			case "IS_LIGHTLEVEL_MIN_6": return 15;
			case "IS_LIGHTLEVEL_MIN_7": return 16;
			case "IS_LIGHTLEVEL_MIN_8": return 17;
			case "IS_LIGHTLEVEL_MIN_9": return 18;
			case "IS_LIGHTLEVEL_MIN_10": return 19;
			case "IS_LIGHTLEVEL_MIN_11": return 20;
			case "IS_LIGHTLEVEL_MIN_12": return 21;
			case "IS_LIGHTLEVEL_MIN_13": return 22;
			case "IS_LIGHTLEVEL_MIN_14": return 23;
			case "IS_LIGHTLEVEL_MIN_15": return 24;
			case "IS_LIGHTLEVEL_MAX_0": return 25;
			case "IS_LIGHTLEVEL_MAX_1": return 26;
			case "IS_LIGHTLEVEL_MAX_2": return 27;
			case "IS_LIGHTLEVEL_MAX_3": return 28;
			case "IS_LIGHTLEVEL_MAX_4": return 29;
			case "IS_LIGHTLEVEL_MAX_5": return 30;
			case "IS_LIGHTLEVEL_MAX_6": return 31;
			case "IS_LIGHTLEVEL_MAX_7": return 32;
			case "IS_LIGHTLEVEL_MAX_8": return 33;
			case "IS_LIGHTLEVEL_MAX_9": return 34;
			case "IS_LIGHTLEVEL_MAX_10": return 35;
			case "IS_LIGHTLEVEL_MAX_11": return 36;
			case "IS_LIGHTLEVEL_MAX_12": return 37;
			case "IS_LIGHTLEVEL_MAX_13": return 38;
			case "IS_LIGHTLEVEL_MAX_14": return 39;
			case "IS_LIGHTLEVEL_MAX_15": return 40;
			case "IS_FOCUSED_ENTITY": return 41;
			case "IS_FOCUSED_BLOCK": return 42;
			case "IS_LIVING": return 43;
			case "IS_HOSTILE": return 44;
			case "IS_NEUTRAL": return 45;
			case "IS_PLAYER": return 46;
			case "IS_ITEM": return 47;
		}
		return 0;
	}
}