package com.example.timerapp.base.domain;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class HistoryService {
    private final List<String> history = new ArrayList<>();

    public List<String> getHistory(){
        List<String> reversed = new ArrayList<>(history);
        Collections.reverse(reversed);
        return reversed;
    }

    public void addTime(String time) {
        if(!history.contains(time)){
            history.add(0, time);
            if(history.size()>4) history.remove(4);
        }
    }
}
