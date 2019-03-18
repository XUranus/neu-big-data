package com.xuranus.demo;

import org.apache.hadoop.hbase.client.Result;

public class School {
    private String chinese_name;
    private String location;
    private String major_department;
    private String found_time;
    private String english_name;
    private String famous_alumni;

    public School(Result result) {
        this.chinese_name = new String(result.getValue("attr".getBytes(),"chinese_name".getBytes()));
        this.location = new String(result.getValue("attr".getBytes(),"location".getBytes()));;
        this.major_department = new String(result.getValue("attr".getBytes(),"major_department".getBytes()));;
        this.famous_alumni = new String(result.getValue("attr".getBytes(),"famous_alumni".getBytes()));;
        this.english_name = new String(result.getValue("attr".getBytes(),"english_name".getBytes()));;
        this.found_time = new String(result.getValue("attr".getBytes(),"found_time".getBytes()));;
    }

    public String getChinese_name() {
        return chinese_name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public String getFamous_alumni() {
        return famous_alumni;
    }

    public String getFound_time() {
        return found_time;
    }

    public String getLocation() {
        return location;
    }

    public String getMajor_department() {
        return major_department;
    }


}
