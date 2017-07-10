package com.zbxn.pub.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * 
 * 
 * @author GISirFive
 * @since 2016-2-16 下午2:19:07
 */
public interface IRequestParams {

	/**
	 * 获取参数集合对应的实体
	 * @return
	 * @author GISirFive
	 */
	public Object getParams();

    /**
     * Adds a key/value string pair to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value);
    
    /**
     * Adds a key/value string pair to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String[] values);

    /**
     * Adds files array to the request.
     *
     * @param key   the key name for the new param.
     * @param files the files array to add.
     * @throws FileNotFoundException if one of passed files is not found at time of assembling the requestparams into request
     */
    public void put(String key, File files[]) throws FileNotFoundException;

    /**
     * Adds a file to the request.
     *
     * @param key  the key name for the new param.
     * @param file the file to add.
     * @throws FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, File file) throws FileNotFoundException;

    /**
     * Adds param with non-string value (e.g. Map, List, Set).
     *
     * @param key   the key name for the new param.
     * @param value the non-string value object for the new param.
     */
    public void put(String key, Object value);

    /**
     * Adds a int value to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value int for the new param.
     */
    public void put(String key, int value);

    /**
     * Adds a long value to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value long for the new param.
     */
    public void put(String key, long value);

    /**
     * Removes a parameter from the request.
     *
     * @param key the key name for the parameter to remove.
     */
    public void remove(String key);


}
