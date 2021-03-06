package com.google.firebase.samples.apps.mlkit;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.samples.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CaptureActivity extends AppCompatActivity {

    public Context context;
    private int IMAGE_CAPTURE_CODE = 1001;
    private Uri imageUri;
    RelativeLayout parent;
    GridView gridView;
    private ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private ImageAdapter imageAdapter;
    private Dialog popup_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        context = this;
        //popup_dialog = new Dialog(this);

        if(savedInstanceState != null){
            bitmaps = savedInstanceState.getParcelableArrayList("images");
            GridView gridView = findViewById(R.id.grid);
            imageAdapter = new ImageAdapter(this,bitmaps);
            gridView.setAdapter(imageAdapter);
        }
        else{
            GridView gridView = findViewById(R.id.grid);
            imageAdapter = new ImageAdapter(this,bitmaps);
            gridView.setAdapter(imageAdapter);
        }



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ContentValues values = new ContentValues();
                //Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            Toast.makeText(context,"Image Clicked!",Toast.LENGTH_LONG).show();
            Bitmap image = (Bitmap) data.getExtras().get("data");
            bitmaps.add(image);
            imageAdapter.notifyDataSetChanged();



        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    public class ImageAdapter extends BaseAdapter{
        ArrayList<Bitmap> bitmaps;
        Context context;
        LayoutInflater inflater;
        ImageAdapter(Context context,ArrayList<Bitmap> bitmaps){
            this.bitmaps = bitmaps;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return bitmaps.size();
        }

        @Override
        public Object getItem(int position) {
            if(bitmaps.size()==0){
                return null;
            }
            return bitmaps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            if(bitmaps.size()==0){
                return null;
            }
            final ImageView imageView = inflater.inflate(R.layout.image,null,false).findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmaps.get(position));
            if(imageView.getParent() != null) {
                ((ViewGroup) imageView.getParent()).removeView(imageView);
            }

            /* Showing a pop up
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup_dialog.setContentView(R.layout.popup);
                    View popup_layout = getLayoutInflater().inflate(R.layout.popup,null);
                    ImageView imageView_popup = (ImageView) popup_layout.findViewById(R.id.image_preview_2);
                    imageView_popup.setImageBitmap(bitmaps.get(position));
                    FloatingActionButton closefloat = (FloatingActionButton) popup_layout.findViewById(R.id.floating_close_2);

                        closefloat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popup_dialog.dismiss();
                            }
                        });

                    popup_dialog.show();
                }
            });

             */
            return imageView;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("images",bitmaps);
    }

}