package com.example.watermark.controller;

import com.example.watermark.tool.OwnImageRemoveWatermarkToolApiConstant;
import com.example.watermark.tool.OwnVideoRemoveWatermarkToolApiConstant;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author laihongfeng
 * @ClassName: VideoController
 * @Description:
 * @date 2023年09月25日 16:32:11
 */
@RequestMapping("/video")
public class VideoController {

    /**
     * 万能去水印
     */
    @PostMapping("/universalRemoveWatermark")
    public Object universalRemoveWatermark (String url) {
        return OwnVideoRemoveWatermarkToolApiConstant.removeWatermark(url);
    }

    /**
     * 万能图集去水印
     */
    @PostMapping("/universalImageRemoveWatermark")
    public Object universalImageRemoveWatermark (String url) {
        return OwnImageRemoveWatermarkToolApiConstant.removeWatermark(url);
    }
}
