package com.Miko_ai.database.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Miko_ai.database.component.Database;

@RestController
public class DbController {
	@Autowired
    private Database database;
	
    @PostMapping("/execute-sql")
    public ResponseEntity<String> executeSql(@RequestBody String sqlStatement) {
    	try {
            boolean b = database.executeSql(sqlStatement);
            if(b == true) {
            	return ResponseEntity.ok("Operation completed successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Operation failed! ");
    }
}
