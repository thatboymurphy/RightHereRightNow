/*
* Author: Stuart Murphy
* Student ID: 10046828
* Project: Masters Thesis
* Date: 08/08/2016
*
* Description:
* The following code is work in progress for my mobile application presentation. It is due
* completion before the 29th of August 2016. This is for supervisors viewing only to see how the
* code is currently looking.
* */

package com.stuart.righthererightnow;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class FragmentB extends Fragment {

    public FragmentB() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, container, false);

        BarChart barChart = (BarChart) view.findViewById(R.id.chart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(PlaceDetail.counterPast6Hours, 0));
        entries.add(new BarEntry(PlaceDetail.counter6to12, 1));
        entries.add(new BarEntry(PlaceDetail.counter12to18, 2));
        entries.add(new BarEntry(PlaceDetail.counter18to24, 3));

        BarDataSet dataset = new BarDataSet(entries, "# of Attendee Posts");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Past 6 hrs");
        labels.add("6-12 Hrs");
        labels.add("12-18 Hrs");
        labels.add("18-24 hrs");
        barChart.setDescription("");

        BarData data = new BarData(labels, dataset);



        dataset.setColor(Color.rgb(255,152,0));

        barChart.setDescription("");
        barChart.setData(data);
        barChart.animateY(3000);
        // get an instance of the YAxis (e.g. left axis)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new MyYAxisValueFormatter());

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setValueFormatter(new MyYAxisValueFormatter());


        leftAxis.setAxisMaxValue(10);
        leftAxis.setAxisMinValue(0);


        rightAxis.setAxisMaxValue(10);
        rightAxis.setAxisMinValue(0);
        rightAxis.setDrawLabels(false);

        dataset.setDrawValues(false);


        Button imagesButton = (Button) view.findViewById(R.id.viewImages);
        imagesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Call on the new activity
                Intent intent = new Intent(v.getContext(), ImageGallery.class);
                startActivity(intent);


            }


        }  );


        Button commentsButton = (Button) view.findViewById(R.id.viewMentions);
        commentsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Call on the new activity
                Intent intent = new Intent(v.getContext(), SocialMediaComments.class);
                startActivity(intent);


            }


        }  );



        return view;
    }


    public class MyYAxisValueFormatter implements YAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter () {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            // write your logic here
            // access the YAxis object to get more information
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }


    }



}
