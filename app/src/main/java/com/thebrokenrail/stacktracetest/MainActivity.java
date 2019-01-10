
package com.thebrokenrail.stacktracetest;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iterations = 10000;

                ArrayList<Long> traceResults = new ArrayList<>();
                ArrayList<Long> noTraceResults = new ArrayList<>();
                for (int i = 0; i < iterations; i++) {
                    traceResults.add(testTrace());
                    noTraceResults.add(testNoTrace());
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                String average = getString(R.string.trace_avg) + " " + average(traceResults) + "\n" + getString(R.string.no_trace_avg) + " " + average(noTraceResults);
                String percent = getString(R.string.percent_start) + " " + (((average(traceResults) - average(noTraceResults)) / average(traceResults)) * 100) + getString(R.string.percent_end);
                builder.setMessage(getString(R.string.iterations) + " " + iterations + "\n\n" + average + "\n\n" + percent);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public double average(ArrayList<Long> list) {
        long total = 0;
        for (int i = 0; i < list.size(); i++) {
            total = total + list.get(i);
        }
        return ((double) total) / list.size();
    }

    private long testTrace() {
        AppCompatSpinner spinner = findViewById(R.id.trace);
        ArrayAdapter<CharSequence> adapter = new TraceAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList(getResources().getTextArray(R.array.planets_array)), spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        long start = System.currentTimeMillis();
        spinner.measure(spinner.getWidth(), spinner.getHeight());
        return System.currentTimeMillis() - start;
    }

    private long testNoTrace() {
        AppCompatSpinner spinner = findViewById(R.id.noTrace);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList(getResources().getTextArray(R.array.planets_array)));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        long start = System.currentTimeMillis();
        spinner.measure(spinner.getWidth(), spinner.getHeight());
        return System.currentTimeMillis() - start;
    }
}
