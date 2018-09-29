package site.binghai.lib.utils;

import net.glxn.qrgen.core.image.ImageType;
import site.binghai.lib.service.QrMaker;

/**
 * @author huaishuo
 * @date 2018/09/28
 */
public class QrUtils {

    public static byte[] toPNG(String text, int width) {
        return new QrMaker(text, width, width).to(ImageType.PNG).stream().toByteArray();
    }
}
