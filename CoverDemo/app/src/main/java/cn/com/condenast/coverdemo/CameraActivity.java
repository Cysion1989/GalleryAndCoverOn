package cn.com.condenast.coverdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera camera;
    private boolean isPreview = false;
    private RecyclerView mRecycler;
    private List<CoverBean> dataList = new ArrayList<>();
    private ImageView mImgLayer;
    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private ImageReader mImageReader;
    private Handler mHandler = new Handler();
    private int mResultCode = 0;
    private Intent mResultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        mImgLayer = (ImageView) findViewById(R.id.img_layer);
        loadData();
    }

    private void loadData() {
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        dataList = CameraHelper.getInstance().getCoverList();
        GlobalDetailAdapter adapter = new GlobalDetailAdapter(this, dataList);
        adapter.setIOnItemClick(new IOnItemClick() {
            @Override
            public void onItemClicker(Object obj, int pos) {
                CoverBean bean = (CoverBean) obj;
                mImgLayer.setImageResource(bean.getTempCover());
            }
        });
        mRecycler.setAdapter(adapter);
        mMediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder aSurfaceHolder) {
        try {
            camera = Camera.open();//打开硬件摄像头，这里导包得时候一定要注意是android.hardware.Camera
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);//得到窗口管理器
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(mHolder);//通过SurfaceView显示取景画面
            camera.startPreview();//开始预览
            isPreview = true;//设置是否预览参数为真
        } catch (IOException e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder aSurfaceHolder, int aI, int aI1, int aI2) {
        focus();
    }

    private void focus() {
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean aB, Camera aCamera) {
            }
        });
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder aSurfaceHolder) {
        if (camera != null) {
            if (isPreview) {//如果正在预览
                camera.stopPreview();
                camera.release();
                isPreview = false;
            }
        }
    }

    public void clip(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(
                    mMediaProjectionManager.createScreenCaptureIntent(),
                    100);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            mResultCode = resultCode;
            mResultData = data;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //开始录制视频
                    mRecycler.setVisibility(View.INVISIBLE);
                    setUpMediaProjection();
                    setUpVirtualDisplay();
                }
            }, 100);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //录制结束，处理数据
                    mRecycler.setVisibility(View.VISIBLE);
                    startCapture();
                }
            }, 200);

        }
    }

    private void setUpMediaProjection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);
        }
    }

    private void setUpVirtualDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImageReader = ImageReader.newInstance(TdScreenUtils.getScreenWidth(this), TdScreenUtils.getScreenHeight(this), PixelFormat.RGBA_8888, 2);
            mMediaProjection.createVirtualDisplay("ScreenCapture",
                    TdScreenUtils.getScreenWidth(this), TdScreenUtils.getScreenHeight(this),
                    4,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
        }
    }

    private void startCapture() {
        Bitmap mBitmap;
        Image image = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            image = mImageReader.acquireLatestImage();
            if (image == null) {
                Toast.makeText(CameraActivity.this, "捕获屏幕失败", Toast.LENGTH_SHORT).show();
                return;
            }
            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            mBitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            mBitmap.copyPixelsFromBuffer(buffer);
            Matrix matrix = new Matrix();
            matrix.postScale(0.5f, 0.5f);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);
            image.close();
            savaCover(mBitmap);
        }
    }

    private void savaCover(Bitmap aMBitmap) {
        if (aMBitmap != null) {
            // 保存或者显示...
            File filer = TdFileUtils.getFile("/test.png");
            FileOutputStream foStream = null;
            try {
                foStream = new FileOutputStream(filer);
                aMBitmap.compress(Bitmap.CompressFormat.PNG, 100, foStream);
                foStream.flush();
                foStream.close();
                Toast.makeText(CameraActivity.this, "图片已保存到" + filer.getPath(), Toast.LENGTH_SHORT).show();
            } catch (Exception aE) {
                aE.printStackTrace();
                Toast.makeText(CameraActivity.this, "拍照失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CameraActivity.this, "拍照失败", Toast.LENGTH_SHORT).show();
        }
    }
}
