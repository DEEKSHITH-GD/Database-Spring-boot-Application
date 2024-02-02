package com.Miko_ai.database.component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class Database {
	private static final String metaDataFile = "metadata.txt";
    private static final String tableFile = "table_data.txt";
    String key;
    String value;
    String retrievedValue;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean executeSql(String sqlStatement) {
        if (sqlStatement.trim().toUpperCase().startsWith("CREATE TABLE")) {
        	boolean b1 = createTable(sqlStatement);
        	if(b1==true) {
        		System.out.println("Table creation successful.");
        		return true;
        	}
        	return false;
        } else if (sqlStatement.trim().toUpperCase().startsWith("INSERT INTO")) {
            boolean b2 = insertIntoTable(sqlStatement);
            if(b2==true) {
            	System.out.println("Data insertion successful.");
            	return true;
            }
            return false;
        } else {
            System.out.println("Invalid SQL statement.");
            return false;
        }
    }

    private boolean createTable(String createTableStatement) {
        try (PrintWriter metadataWriter = new PrintWriter(new FileWriter(metaDataFile))) {
            String[] tokens = createTableStatement.split("\\(")[1].split(",");
            key = tokens[0].trim();
            value = tokens[1].trim();
            redisTemplate.opsForValue().set(key, value);
            
            for (String token : tokens) {
                String[] columnInfo = token.trim().split("\\s+");
                metadataWriter.println(columnInfo[0] + " " + columnInfo[1].toUpperCase());
            }
            retrievedValue = redisTemplate.opsForValue().get(key);
            if(retrievedValue != null) {
            	System.out.println("Value retrieved from Redis for key " + key + ": " + retrievedValue);
            	return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean insertIntoTable(String insertStatement) {
        try {
            String values = insertStatement.split("VALUES")[1].trim();
            System.out.println("values: "+values);
            String[] dataValues = values.substring(1, values.length() - 1).split(",");
            key = dataValues[0].trim();
            value = dataValues[1].trim();
            redisTemplate.opsForValue().set(key, value);
            retrievedValue = redisTemplate.opsForValue().get(key);
            if(retrievedValue != null) {
            	System.out.println("Value retrieved from Redis for key " + key + ": " + retrievedValue);
            	return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
