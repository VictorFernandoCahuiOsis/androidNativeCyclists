package com.example.proyectoplataformas.model;

import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    private ImageView imag;
    private float scale = 1f;

    public ScaleListener(ImageView iv){
        this.imag = iv;
    }

    public boolean onScale(ScaleGestureDetector detector){
        scale *= detector.getScaleFactor();
        imag.setScaleX(scale);
        imag.setScaleY(scale);
        return true;
    }
}