package site.binghai.lib.service;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import net.glxn.qrgen.core.AbstractQRCode;
import net.glxn.qrgen.core.exception.QRGenerationException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author huaishuo
 * @date 2018/09/28
 */
public class QrMaker extends AbstractQRCode {
    private String text;

    public QrMaker(String text, int w, int h) {
        this.text = text;
        qrWriter = new QRCodeWriter();
        height = h;
        width = w;
    }

    @Override
    public File file() {
        File file;
        try {
            file = createTempFile();
            MatrixToImageWriter.writeToPath(createMatrix(text), imageType.toString(), file.toPath());
        } catch (Exception e) {
            throw new QRGenerationException("Failed to create QR image from text due to underlying exception", e);
        }

        return file;
    }

    @Override
    public File file(String name) {
        File file;
        try {
            file = createTempFile(name);
            MatrixToImageWriter.writeToPath(createMatrix(text), imageType.toString(), file.toPath());
        } catch (Exception e) {
            throw new QRGenerationException("Failed to create QR image from text due to underlying exception", e);
        }

        return file;
    }

    @Override
    protected void writeToStream(OutputStream stream) throws IOException, WriterException {
        MatrixToImageWriter.writeToStream(createMatrix(text), imageType.toString(), stream);
    }
}