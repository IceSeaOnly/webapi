package site.binghai.biz.controller;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import net.glxn.qrgen.core.AbstractQRCode;
import net.glxn.qrgen.core.exception.QRGenerationException;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import site.binghai.biz.entity.QrcodeImageMakeLog;
import site.binghai.biz.service.QrcodeImageMakeLogService;
import site.binghai.lib.controller.BaseController;
import site.binghai.lib.service.QrMaker;
import site.binghai.lib.utils.QrUtils;
import site.binghai.lib.utils.UrlUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by binghai on 2018/1/27.
 *
 * @ api_debugger
 */
@RequestMapping("/")
@Controller
public class qrCodeController extends BaseController {
    @Autowired
    private QrcodeImageMakeLogService logService;


    @RequestMapping("qrCode")
    public void makeQrCode(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        String qrtext = request.getParameter("t");

        QrcodeImageMakeLog log = new QrcodeImageMakeLog();
        log.setSourceIp(UrlUtil.getIpAdrress(request));
        log.setSourceUrl(UrlUtil.getFullUrl(request));
        log.setCtx(qrtext);
        logService.save(log);

        int w = 300;
        try {
            String ws = request.getParameter("w");
            if (!hasEmptyString(ws)) {
                w = Integer.parseInt(ws);
            }
        } catch (Exception e) {
        }

        byte[] png = QrUtils.toPNG(qrtext, w);

        response.setContentType("image/png");
        response.setContentLength(png.length);

        OutputStream outStream = response.getOutputStream();

        outStream.write(png);

        outStream.flush();
        outStream.close();
    }
}

