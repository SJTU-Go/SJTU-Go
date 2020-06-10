package org.sjtugo.api.config;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class JsonDateTimeValueProcessor implements JsonValueProcessor {


    private String datePattern = "yyyy-MM-dd HH:mm:ss";


    public JsonDateTimeValueProcessor() {
        super();
    }


    public JsonDateTimeValueProcessor(String format) {
        super();
        this.datePattern = format;
    }


    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return process(value);
    }


    @Override
    public Object processObjectValue(String key, Object value,
                                     JsonConfig jsonConfig) {
        return process(value);
    }

    private Object process(Object value) {
        try {
            if (value instanceof LocalDateTime) {
                DateTimeFormatter sdf = DateTimeFormatter.ofPattern(datePattern);
                return sdf.format((LocalDateTime) value);
            }
            return value == null ? "" : value.toString();
        } catch (Exception e) {
            return "";
        }
    }


    public String getDatePattern() {
        return datePattern;
    }


    public void setDatePattern(String pDatePattern) {
        datePattern = pDatePattern;
    }

}