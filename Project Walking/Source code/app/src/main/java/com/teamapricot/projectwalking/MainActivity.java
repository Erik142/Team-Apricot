package com.teamapricot.projectwalking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.teamapricot.projectwalking.handlers.CameraHandler;
import com.teamapricot.projectwalking.photos.PhotoController;

public class MainActivity extends AppCompatActivity {
    private PhotoController photoController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissionHandler permissionHandler = new PermissionHandler(this);
        CameraHandler cameraHandler = new CameraHandler(this);

        photoController = new PhotoController(this, permissionHandler, cameraHandler);

        setContentView(R.layout.activity_main);
    }

    public void captureImage(View view) {
        photoController.captureImageAsync();
    }
}