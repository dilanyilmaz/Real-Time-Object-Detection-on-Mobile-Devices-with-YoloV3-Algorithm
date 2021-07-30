package com.example.yolood;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

import org.opencv.dnn.Dnn;
import org.opencv.utils.Converters;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;







public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 , TextToSpeech.OnInitListener {
    //izinler
    private static final String[] READ_PERMISSION= {Manifest.permission.READ_EXTERNAL_STORAGE
                                                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                    ,Manifest.permission.CAMERA};
    private static final int REQUEST_CODE=1;
    private static final String TAG = "MainActivity";

    /*Nesneleri sesli okuma yaptırma*/
    TextToSpeech tts;
    Context context = this;
    /*------------------------------*/
    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    boolean startYolo = false;
    boolean firstTimeYolo = false;
    Net tinyYolo;
    boolean startdeneme = false;

    List<String> cocoNames = Arrays.asList(
            "a person","a bicycle","a motorbike","an airplane", "a bus",
            "a train", "a truck", "a boat", "a traffic light", "a fire hydrant", "a stop sign",
            "a parking meter", "a car", "a bench", "a bird", "a cat", "a dog", "a horse",
            "a sheep", "a cow", "an elephant", "a bear", "a zebra", "a giraffe", "a backpack",
            "an umbrella", "a handbag", "a tie", "a suitcase", "a frisbee", "skis", "a snowboard",
            "a sports ball", "a kite", "a baseball bat", "a baseball glove", "a skateboard",
            "a surfboard", "a tennis racket", "a bottle", "a wine glass", "a cup",
            "a fork", "a knife", "a spoon", "a bowl", "a banana", "an apple",
            "a sandwich", "an orange", "broccoli", "a carrot", "a hot dog",
            "a pizza", "a doughnut", "a cake", "a chair", "a sofa", "a potted plant",
            "a bed", "a dining table", "a toilet", "a TV monitor", "a laptop",
            "a computer mouse", "a remote control", "a keyboard", "a cell phone",
            "a microwave", "an oven", "a toaster", "a sink", "a refrigerator",
            "a book", "a clock", "a vase", "a pair of scissors", "a teddy bear",
            "a hair drier", "a toothbrush");
    int idGuy;

    public void deneme(View Button){

        tts = new TextToSpeech(context,this);
        converttoTextToSpeech();
    }

    public void YOLO(View Button){

        if (startYolo == false){

            startYolo = true;

            if (firstTimeYolo == false){
                firstTimeYolo = true;
                String tinyYCfg = getPath(".cfg", this.getApplicationContext());
                String tinyYWeights = getPath(".weights", this.getApplicationContext());

                tinyYolo = Dnn.readNetFromDarknet(tinyYCfg, tinyYWeights);
            }
        }

        else{
            startYolo = false;
        }

    }

    private static String getPath(String fileType, Context context) {
        AssetManager assetManager = context.getAssets();
        String[] pathNames = {};
        String fileName = "";
        System.out.println("-----------------------------------------------------------------------");
        try {
            pathNames = assetManager.list("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for ( String filePath : pathNames ) {
            System.out.println(filePath);
            if ( filePath.endsWith(fileType)) {
                fileName = filePath;
                break;
            }
        }
        BufferedInputStream inputStream;
        try {
            // Read data from assets.
            inputStream = new BufferedInputStream(((AssetManager) assetManager).open(fileName));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();

            // Create copy file in storage.
            File outFile = new File(context.getFilesDir(), fileName);
            FileOutputStream os = new FileOutputStream(outFile);
            os.write(data);
            os.close();
            // Return a path to file which may be read in common way.
            return outFile.getAbsolutePath();
        } catch (IOException ex) {
            //Log.i(TAG, "Failed to upload a file");
        }
        return "";
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);



        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);

                switch(status){

                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };



    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat frame = inputFrame.rgba();

        if (startYolo == true) {

            Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
            Mat imageBlob = Dnn.blobFromImage(frame, 0.00392, new Size(416,416),new Scalar(0, 0, 0),/*swapRB*/false, /*crop*/false);
            tinyYolo.setInput(imageBlob);
            java.util.List<Mat> result = new java.util.ArrayList<Mat>(2);

            List<String> outBlobNames = new java.util.ArrayList<>();
            outBlobNames.add(0, "yolo_16");
            outBlobNames.add(1, "yolo_23");

            tinyYolo.forward(result,outBlobNames);

            float confThreshold = 0.3f;

            List<Integer> clsIds = new ArrayList<>();
            List<Float> confs = new ArrayList<>();
            List<Rect> rects = new ArrayList<>();

            for (int i = 0; i < result.size(); ++i)
            {
                Mat level = result.get(i);
                for (int j = 0; j < level.rows(); ++j)
                {
                    Mat row = level.row(j);
                    Mat scores = row.colRange(5, level.cols());
                    Core.MinMaxLocResult mm = Core.minMaxLoc(scores);
                    float confidence = (float)mm.maxVal;
                    Point classIdPoint = mm.maxLoc;

                    if (confidence > confThreshold)
                    {
                        int centerX = (int)(row.get(0,0)[0] * frame.cols());
                        int centerY = (int)(row.get(0,1)[0] * frame.rows());
                        int width   = (int)(row.get(0,2)[0] * frame.cols());
                        int height  = (int)(row.get(0,3)[0] * frame.rows());

                        int left    = centerX - width  / 2;
                        int top     = centerY - height / 2;

                        clsIds.add((int)classIdPoint.x);
                        confs.add((float)confidence);

                        rects.add(new Rect(left, top, width, height));
                    }
                }
            }
            int ArrayLength = confs.size();

            if (ArrayLength>=1) {
                // Apply non-maximum suppression procedure.
                float nmsThresh = 0.2f;
                MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));
                Rect[] boxesArray = rects.toArray(new Rect[0]);
                MatOfRect boxes = new MatOfRect(boxesArray);
                MatOfInt indices = new MatOfInt();

                Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices);

                // Draw result boxes:
                int[] ind = indices.toArray();
                for (int i = 0; i < ind.length; ++i) {

                    int idx = ind[i];
                    Rect box = boxesArray[idx];
                    idGuy = clsIds.get(idx);
                    float conf = confs.get(idx);



                    int intConf = (int) (conf * 100);
                    Imgproc.putText(frame,cocoNames.get(idGuy) + " " + intConf + "%",box.tl(),Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255,255,0),2);
                    Imgproc.rectangle(frame, box.tl(), box.br(), new Scalar(255, 0, 0), 2);




                }
            }
        }
        return frame;
    }


    @Override
    public void onCameraViewStarted(int width, int height) {



        if (startYolo == true){
           // String tinyYCfg = Environment.getExternalStorageDirectory() + "/dnns/yolov3-tiny.cfg" ;
           // String tinyYWeights = Environment.getExternalStorageDirectory() + "/dnns/yolov3-tiny.weights";
            String tinyYCfg = getPath(".cfg", this.getApplicationContext());
            String tinyYWeights = getPath(".weights", this.getApplicationContext());
            tinyYolo  = Dnn.readNetFromDarknet(tinyYCfg, tinyYWeights);
        }

    }


    @Override
    public void onCameraViewStopped() {}

    @Override  //Uygulama çalıştığında izinleri isteme işlemini start a yazıyoruz.
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart: Uygulama izni kontrol edilecek..");
        checkPermissionStorage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //kamera calısma esnasında hata alıp almadığımızı kontrol ediyoruz.
        if (!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"protected void onResume da hata var!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override /*başlangıç metodu seslendirme içim*/
    public void onInit(int i) {
        if(i==tts.SUCCESS){
            int sonuc = tts.setLanguage(Locale.ENGLISH);
            if(sonuc==tts.LANG_MISSING_DATA || sonuc== tts.LANG_NOT_SUPPORTED){
                Toast.makeText(context," ", Toast.LENGTH_SHORT).show();
            }else{
                //metot
                converttoTextToSpeech();
            }

        }else{
            Toast.makeText(context,"Başarısız", Toast.LENGTH_SHORT).show();
        }
    }

    private void converttoTextToSpeech(){
        String text = cocoNames.get(idGuy).toString();

        if(null== text || "".equals(text)){
            Toast.makeText(context,"nesne tanımlanamadı",Toast.LENGTH_SHORT).show();

        }
            tts.speak(text,tts.QUEUE_FLUSH,null);

    }

    private void checkPermissionStorage(){
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),READ_PERMISSION[0])== PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this.getApplicationContext(),READ_PERMISSION[1])== PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this.getApplicationContext(),READ_PERMISSION[2])== PackageManager.PERMISSION_GRANTED){
            Log.d(TAG,"checkPermissionStorage: Uygulamanın okuma izni mevcut.Dosya yolları okunacak");
            //Uygulama Akışı
        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(this,READ_PERMISSION[0]))
        {
            showPermissionDialog();
            Log.d(TAG,"checkPermissionStorage: Uygulama izni için gerekçe gösteriliyor");
        }
        else{
            Log.d(TAG,"checkPermissionStorage: Uygulama izni yok. Kullanıcıdan izin alınıyor.");
            ActivityCompat.requestPermissions(this,READ_PERMISSION,REQUEST_CODE);
        }
    }
    private void showPermissionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_permission_title2);
        builder.setMessage(R.string.dialog_permission_body2);
        builder.setIcon(R.drawable.ic_settings_24);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.exit_app, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton(R.string.allow_app, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this, READ_PERMISSION, REQUEST_CODE);
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE)
        {
            Log.d(TAG, "onRequestPermissionsResult: Uygulamanın okuma izninin sonucu");
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: Uygulamaya izin verildi.Son kontroller yapılıyor.");
                checkPermissionStorage();
            }
            else {
                showPermissionDialog();
                Log.d(TAG, "onRequestPermissionsResult: Uygulamanın okuma izni mevcut.Dosya yolları okunacak");
            }
        }
    }
}