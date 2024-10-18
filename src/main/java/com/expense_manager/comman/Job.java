package com.expense_manager.comman;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Job implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String user;
    private String lockKey;

    private Map<String, Object> attributes = new LinkedHashMap<>();

    public Job(String name, String lockKey) {
        this.name = name;
        this.lockKey = lockKey;
    }


    public String getLockKey() {
        return lockKey;
    }
    
    public void put(String key, Long value) {
        attributes.put(key, value);
    }

    public void put(String key, Integer value) {
        attributes.put(key, value);
    }

    public void put(String key, String value) {
        attributes.put(key, value);
    }

    public Object get(String key) {
        return attributes.get(key);
    }

    public Long getLong(String key) {
        // return (Long) get(key);
         return Long.parseLong(get(key).toString());
     }


}