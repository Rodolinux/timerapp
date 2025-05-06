package com.example.timerapp.base.ui.component;

import com.vaadin.flow.component.html.Div;

//import static com.vaadin.copilot.javarewriter.JavaStyleRewriter.setStyle;

public class TimerDisplay extends Div {
    public TimerDisplay(){
        setText("00:00");
        getStyle().set("font-size", "2em").set("margin", "20 px");
    }
    public void update(int totalSeconds){
        int mm = totalSeconds / 60;
        int ss = totalSeconds % 60;
        setText(String.format("%02d:%02d", mm, ss));
    }
}
