package com.example.timerapp.base.ui.view;

import com.example.timerapp.base.domain.HistoryService;
import com.example.timerapp.base.ui.component.TimerDisplay;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.example.timerapp.base.domain.HistoryService;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Route("")
public class MainView extends VerticalLayout {
    private final TimerDisplay timerDisplay = new TimerDisplay();
    private final TextField timeInput = new TextField("Tiempo inicial (mm:ss)");
    private final Button startButton = new Button("Iniciar");

    private final HistoryService historyService;
    private Registration timerRegistration;
    private int totalSeconds = 0;

    public MainView(HistoryService historyService) {
        this.historyService = historyService;

        HorizontalLayout inputLayout = new HorizontalLayout();
        inputLayout.add(timeInput, startButton);

        Div historyContainer = new Div();


        startButton.addClickListener(event -> {
            String input = timeInput.getValue();
            if (input.matches("\\d+\\:\\d{2}")) {
                String[] parts = input.split(":");
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                totalSeconds = minutes * 60 + seconds;
                historyService.addTime(input);
                updateHistory(historyContainer);
                startTimer();
            }
        });

        add(new H2("Temporizador"), inputLayout, timerDisplay, new Div("Historial:"), historyContainer);

    }
    private void updateTimerDisplay() {
        int mm = totalSeconds / 60;
        int ss = totalSeconds % 60;
        timerDisplay.setText(String.format("%02d:%02d", mm, ss));
    }

    private void startTimer() {
        if (timerRegistration != null) {
            timerRegistration.remove();
        }

        UI.getCurrent().setPollInterval(1000);

        timerRegistration = UI.getCurrent().addPollListener(event -> {
            if (totalSeconds > 0) {
                totalSeconds--;
                timerDisplay.update(totalSeconds);
                //updateTimerDisplay();
            } else {
                timerRegistration.remove();
                UI.getCurrent().setPollInterval(-1);
            }
        });
    }

    private void updateHistory(Div container) {
        List<String> history = historyService.getHistory();
        container.removeAll();

        for (String t : history) {
            Button btn = new Button(t);
            btn.addClickListener(e -> {
                timeInput.setValue(t);
                String[] parts = t.split(":");
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                totalSeconds = minutes * 60 + seconds;
                timerDisplay.update(totalSeconds);
                startTimer();
            });
            container.add(btn);
        }
    }
}
