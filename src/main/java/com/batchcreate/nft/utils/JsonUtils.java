package com.batchcreate.nft.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(df);
        objectMapper.setTimeZone(TimeZone.getTimeZone("PRC"));
    }

    public static ObjectMapper getObjectMapper(){
        return objectMapper;
    }

    @Nullable
    public static String object2Json(Object o){
        try {
            return writeValue(o);
        } catch (Exception e) {
            log.warn("convert object to string exception:", e);
            log.warn("failed object:{}", o);
            return null;
        }
    }

    @Nullable
    public static <T> T json2Object(String s, Class<T> clazz){
        try {
            return parse(s, clazz);
        } catch (Exception e) {
            log.warn("parse json string to object exception:",e);
            log.warn("failed string:{}", s);
            return null;
        }
    }

    @Nullable
    public static <T> List<T> json2List(String s, Class<T> clazz){
        try {
            if (Strings.isBlank(s)) {
                return new ArrayList<>();
            }
            @SuppressWarnings("unchecked")
            T[] array = (T[]) parse(s, Array.newInstance(clazz, 0).getClass());
            ArrayList<T> list = new ArrayList<>(array.length);
            Collections.addAll(list, array);
            return list;
        } catch (Exception e) {
            log.warn("parse json string to list exception:",e);
            log.warn("failed string:{}", s);
            return null;
        }
    }


    public static <T> T parse(String data, Class<T> clazz) throws IOException {
        return objectMapper.readValue(data, clazz);
    }


    public static String writeValue(Object value) throws IOException {
        return objectMapper.writeValueAsString(value);
    }





    /**
     * 数组字符串转List 待验证
     *
     * @param str
     * @return
     */
    public static List<String> StringToStrList(String str) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, String.class);
        try {
            List<String> strList = objectMapper.readValue(str, javaType);
            return strList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数组字符串转List 待验证
     *
     * @param str
     * @return
     */
    public static List<Long> StringToLongList(String str) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
        try {
            List<Long> strList = objectMapper.readValue(str, javaType);
            return strList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数组字符串转List 待验证
     *
     * @param str
     * @return
     */
    public static List<Integer> StringToIntList(String str) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Integer.class);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
