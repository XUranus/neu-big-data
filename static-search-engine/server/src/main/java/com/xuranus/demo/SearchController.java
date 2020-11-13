package com.xuranus.demo;

import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/api",method = RequestMethod.POST)
public class SearchController {

    @PostMapping("/search")
    public Map handleSearch(@RequestBody Map req) {
        String key = (String)req.get("input");
        System.out.println("search="+key);
        HbaseSearch hbaseSearch = new HbaseSearch();
        Map m = new HashMap();

        String[] keys = hbaseSearch.getLinksByKeyword(key).split(";");
        ArrayList schools = new ArrayList();
        for(int i=0;i<keys.length-1;i++) {
            String schoolName = keys[i].split(":")[0];
            String num = keys[i].split(":")[1];
            School school = hbaseSearch.getSchoolInfo(schoolName);
            schools.add(school);
        }
        m.put("data",schools);
        return m;
    }


}
