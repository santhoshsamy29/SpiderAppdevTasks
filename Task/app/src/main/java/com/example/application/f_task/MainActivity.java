package com.example.application.f_task;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    MyDataBase myDataBase = new MyDataBase(this);
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    ImageAdapter img_adapter;
    FloatingActionButton fab;
    String timeStamp;
    String imgname;

    private static final int REQUEST_CAMERA = 1;
    public int POSITION = 0;

    public int snack_flag;

    ArrayList<CardView> img_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);



        SharedPreferences pref = getSharedPreferences("POS",Context.MODE_PRIVATE);
        POSITION = pref.getInt("Position",0);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordi_layout);

        if(POSITION == 0){
            Snackbar message = Snackbar.make(coordinatorLayout, "Click the button on the right to add images", Snackbar.LENGTH_LONG);
            message.show();
        }

        recyclerView = (RecyclerView) findViewById(R.id.rv_view);
        fab = (FloatingActionButton)findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] cam_permission = new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                if(request(cam_permission,REQUEST_CAMERA)){
                    cameraIntent();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3);

        recyclerView.setLayoutManager(layoutManager);
        img_list = myDataBase.getData();

        img_adapter = new ImageAdapter(this,img_list,size.x);
        recyclerView.setAdapter(img_adapter);

        img_adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(CardView card) {

                Intent oIntent = new Intent(MainActivity.this,ImageOpen.class);
                oIntent.putExtra("Image",img_list.indexOf(card));
                startActivity(oIntent);
            }
        });

        initswipe();
    }


    public boolean request(String[] permission,int perm_id){
        ArrayList<String> temp_perm = new ArrayList<>();
        for(int i=0 ; i<permission.length ; i++) {
            if (ContextCompat.checkSelfPermission(this, permission[i])
                    != PackageManager.PERMISSION_GRANTED) {
                temp_perm.add(permission[i]);
            }
        }

        String[] perm = new String[temp_perm.size()];
        perm = temp_perm.toArray(perm);

        if(perm.length>0){
            ActivityCompat.requestPermissions(this,perm,perm_id);
            return false;
        }
        else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "GRANT ALL PERMISSIONS", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        switch (requestCode) {
            case REQUEST_CAMERA:
                cameraIntent();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void cameraIntent(){
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imgname = "IMG_" + timeStamp + ".jpg";
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(camIntent.resolveActivity(getPackageManager())!= null){
            File img_path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/Spider");
            if(!img_path.exists()){
                if(img_path.mkdirs()){
                    Log.d("CAE","Failed to create Directory");
                }
            }
            File image_name = new File(img_path,imgname);
            Uri img_uri = Uri.fromFile(image_name);
            //Log.e("SAN","Camera Intent " + img_uri);
            camIntent.putExtra(MediaStore.EXTRA_OUTPUT,img_uri);
            startActivityForResult(camIntent,REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    File img_path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/Spider");
                    File image_name = new File(img_path,imgname);

                    Bitmap bm = decodebitmap(image_name,700,1000);

                    fOut(bm,image_name);

                    Uri imageUri = getContent(this,image_name);
                    CardView new_card = new CardView(POSITION,imageUri);
                    img_list.add(new_card);
                    myDataBase.createData(new_card);
                    img_adapter.notifyDataSetChanged();
                    POSITION++;
                    break;
            }
        }
    }


    private Uri getContent(Context context, File image) {

        String imagePath = image.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Files.getContentUri("external"),
                new String[] { MediaStore.Files.FileColumns._ID },
                MediaStore.Files.FileColumns.DATA + "=? ",
                new String[] { imagePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Files.getContentUri("external"), "" + id);
        } else {
            if (image.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DATA, imagePath);
                return context.getContentResolver().insert(
                        MediaStore.Files.getContentUri("external"), values);
            } else {
                return null;
            }
        }
    }




    public void initswipe() {
        final ItemTouchHelper.SimpleCallback ith = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final CardView temp_card = img_list.get(position);
                img_list.remove(position);
                img_adapter.notifyItemRemoved(position);
                Snackbar sbar = Snackbar.make(coordinatorLayout, "Image removed", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                img_list.add(position,temp_card);
                                img_adapter.notifyItemInserted(position);
                                snack_flag=1;
                            }
                        })
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                if (snack_flag != 1) {
                                    myDataBase.removeRow(temp_card);
                                }
                                snack_flag = 0;
                                super.onDismissed(transientBottomBar, event);
                            }
                        });
                sbar.show();

            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(ith);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public static Bitmap decodebitmap(File image, int rWidth, int rHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getAbsolutePath(),options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        int samplesize = 1;

        if(height > rHeight)
        {
            samplesize = Math.round((float)height/(float)rHeight);
        }
        int expecWidth = width/samplesize;
        if(expecWidth > rWidth)
        {
            samplesize = Math.round((float)width/(float)rWidth);
        }

        options.inSampleSize = samplesize;

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(image.getAbsolutePath(),options);
    }

    public void fOut(Bitmap bm, File image){
        try {
            OutputStream oStream = new FileOutputStream(image);
            bm.compress(Bitmap.CompressFormat.PNG,100,oStream);
            try {
                oStream.flush();
                oStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDataBase.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences("POS", Context.MODE_PRIVATE);
        Log.e("SAN","Position =" + POSITION);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("Position",POSITION);
        editor.apply();
    }

    public interface OnClickListener{
        void onClick(CardView card);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
