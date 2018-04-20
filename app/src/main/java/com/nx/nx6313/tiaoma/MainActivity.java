package com.nx.nx6313.tiaoma;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    private TextView isbn;
    private EditText tiaomainfo;
    private ImageView createdtiaoma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isbn = findViewById(R.id.isbn);
        tiaomainfo = findViewById(R.id.tiaomainfo);
        createdtiaoma = findViewById(R.id.createdtiaoma);
    }

    public void createTiaoma(View view) {
        String info = tiaomainfo.getText().toString().trim();
        if (!info.equals("")) {
            Bitmap a = encodeAsBitmap(info);
            createdtiaoma.setImageBitmap(a);
        }
    }

    public void sanma(View view) {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCaptureActivity(ScanActivity.class); // 设置打开摄像头的Activity
        integrator.setPrompt("请扫描库存商品条形码"); // 底部的提示文字，设为""可以置空
        integrator.setCameraId(0); // 前置或者后置摄像头
        integrator.setBeepEnabled(true); // 扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    Bitmap encodeAsBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            // 查看各参数解析
            result = multiFormatWriter.encode(str, BarcodeFormat.CODE_128, 800, 300);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) {
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            isbn.setText(result);
        }
    }
}
