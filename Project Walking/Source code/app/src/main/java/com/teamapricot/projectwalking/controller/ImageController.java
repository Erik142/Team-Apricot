package com.teamapricot.projectwalking.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.teamapricot.projectwalking.R;

import java.io.File;

/**
 * @author Daniel Brännvall
 * @version 2021-10-04
 */
public class ImageController {
    private final static String TAG = "ImageController";

    private final ImageView imageView;

    public ImageController(AppCompatActivity activity) {
        imageView = activity.findViewById(R.id.image_view);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setVisibility(View.GONE);
        imageView.setOnClickListener(this::closeImage);
    }

    /**
     * Views the specified image file on screen.
     *
     * @param file - The file to view
     */
    public void viewImageFile(File file) {
        Bitmap image = BitmapFactory.decodeFile(file.getPath());
        imageView.setImageBitmap(image);
        imageView.setVisibility(View.VISIBLE);
        imageView.invalidate();
    }

    /**
     * Removes the specified view.
     *
     * @param view - The view to remove
     */
    public void closeImage(View view) {
        view.setVisibility(View.GONE);
        view.invalidate();
    }
}
