/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;

/**
 *
 * @author mingxuan
 */
public class Tasks implements Serializable {
    
 private String input;
 private String input_format;
 private String profile;

    public Tasks() {
    }

    public Tasks(String input, String input_format, String profile) {
        this.input = input;
        this.input_format = input_format;
        this.profile = profile;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getInput_format() {
        return input_format;
    }

    public void setInput_format(String input_format) {
        this.input_format = input_format;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
    
}
