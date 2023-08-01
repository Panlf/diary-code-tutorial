package com.plf.diary.activiti;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author panlf
 * @date 2023/7/27
 */
public class SvgToPngConverter {
    public static void main(String[] args) {
        String svgFilePath = "C:\\Users\\Breeze\\Downloads\\copy.svg";
        String pngFilePath = "C:\\Users\\Breeze\\Downloads\\copy.png";
        try {
            InputStream in = new FileInputStream(svgFilePath);
            Transcoder transcoder = new PNGTranscoder();
            //设置png图片的宽和长
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 400f);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 800f);
            TranscoderInput input = new TranscoderInput(in);
            TranscoderOutput output = new TranscoderOutput(new FileOutputStream(pngFilePath));
            transcoder.transcode(input, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
