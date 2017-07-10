package com.zbxn.pub.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.StringUtils;

public class JsonUtil {

	public static Gson gson = null;

	private JsonUtil() {
	}

	/**
	 * <b>获取带有条件限制的GSON实例</b></br> 1、不导出实体中没有用@Expose注解的属性</br>
	 * 2、时间转化为特定格式（yyyy-MM-dd HH:mm）</br> 3、转换失败的属性值为null
	 * 
	 * @return
	 */
	public static Gson getInstance() {
		if (gson == null) {
			GsonBuilder builder = new GsonBuilder();
			// 不导出实体中没有用@Expose注解的属性
			builder.excludeFieldsWithoutExposeAnnotation();
			// 时间转化为特定格式
			builder.setDateFormat("yyyy-MM-dd HH:mm");
			// 转换失败的属性值为null
			builder.serializeNulls();
			// 对json结果格式化
			builder.setPrettyPrinting();
			gson = builder.create();
		}
		return gson;
	}

	/**
	 * <b>获取普通GSON实例</b></br>
	 * 
	 * @return
	 */
	public static Gson getNormalInstance() {
		return new Gson();
	}

	/**
	 * 实体类转为json字符串
	 *
	 * @param obj
	 * @return
	 */
	public static String toJsonString(Object obj) {
		String temp = getInstance().toJson(obj);
		return temp;
	}

	/**
	 * 将json字符串转换为指定类的实例</br> 转换失败时返回null
	 * 
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T> T fromJsonString(String json, Class<T> classOfT) {
		T t = null;
		try {
			t = getInstance().fromJson(json, classOfT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 将json字符串转换为指定类的实例的集合</br> 转换失败时返回null
	 * 
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T> List<T> fromJsonList(String json, Class<T> classOfT) {
		ArrayList<T> list = new ArrayList<T>();

		JsonArray array = null;
		try {
			array = new JsonParser().parse(json).getAsJsonArray();
		} catch (Exception e) {
			return list;
		}
		if (array.size() == 0)
			return list;
		for (JsonElement element : array) {
			try {
				T t = getInstance().fromJson(element, classOfT);
				list.add(t);
			} catch (JsonSyntaxException e) {
			}
		}
		return list;
	}

	/**
	 * 是否是正确的json格式  true--是
	 * @param json
	 * @return
	 */
	public static boolean isGoodJson(String json) {
		if (StringUtils.isEmpty(json)) {
			return false;
		}
		try {
			new JsonParser().parse(json);
			return true;
		} catch (JsonParseException e) {
			System.out.println("错误的json格式:" + json);
			return false;
		}
	}

	/**
	 * 获取json中用key获取对应list的json
	 *
	 * @param jsonString
	 * @param key
	 * @return
	 */
	public static String getArrayString(String jsonString, String key) {
		String value = "";
		JSONObject json;
		try {
			json = new JSONObject(jsonString);
			value = json.getJSONArray(key).toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 获取json中用key获取对应值
	 *
	 * @param jsonString
	 * @param key
	 * @return
	 */
	public static String getString(String jsonString, String key) {
		String value = "";
		JSONObject json;
		try {
			json = new JSONObject(jsonString);
			value = json.getString(key);
		} catch (Exception e) {
			value = "";
			e.printStackTrace();
		}
		return value;
	}

}
