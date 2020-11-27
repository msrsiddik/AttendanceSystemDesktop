package msr.attend.desktop;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class QRScanner  implements Runnable, ThreadFactory {
    private Webcam webcam = null;
    private Executor executor = Executors.newSingleThreadExecutor(this);
    private String oldText = "";

    public void initWebcam() {
        Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0); //0 is default webcam
        new WebcamPanel(webcam);
        webcam.setViewSize(size);
        executor.execute(this);
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {
                if ((image = webcam.getImage()) == null) {
                    continue;
                }
            }

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                result = new MultiFormatReader().decode(bitmap);
            } catch (NotFoundException e) {
                //No result...
            }

            if (result != null) {
                if (!result.getText().equals(oldText)){
                    oldText = result.getText();
//                    System.out.println(result.getText());
                    VirtualCardScanData data = new MainUIController();
                    data.getUIDListener(result.getText());
                } else {
//                    System.out.println("Already Scan");
                    try {
                        Thread.sleep(5000);
                        oldText="";
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);
        return t;
    }

    interface VirtualCardScanData{
        void getUIDListener(String uid);
    }

}
