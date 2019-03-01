package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    ImageView slideImageView;
    TextView slideHeading;
    TextView slideDescription;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    //Array........
    public int[] slide_images = {
            R.drawable.safety,
            R.drawable.speed,
            R.drawable.rescue,
            R.drawable.eat_icon
    };
    public String[] slide_headings = {
            "SAFETY",
            "SPEED",
            "SERVICE",
            "SIMPLE"
    };
    public String[] slide_descs = {
            " Help, you in the danger time , by sending sms to your closed ones .",

            "Takes seconds to work",

            "Realtime location service.so, that rescuer don't miss you out.",

            "Easy to operate."


    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);
        slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        slideDescription = (TextView) view.findViewById(R.id.slide_desc);
        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}