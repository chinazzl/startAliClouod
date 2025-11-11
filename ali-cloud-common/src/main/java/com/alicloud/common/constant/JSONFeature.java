package com.alicloud.common.constant;


import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;

/**
 * @author: zhaolin
 * @Date: 2024/12/8
 **/
public class JSONFeature {

    public static final JSONWriter.Feature[] FASTJSON2_WRITE_FEATURES = new JSONWriter.Feature[]{
            JSONWriter.Feature.WriteMapNullValue,
            JSONWriter.Feature.WriteNullListAsEmpty,
            JSONWriter.Feature.WriteNullStringAsEmpty,
            JSONWriter.Feature.WriteEnumUsingToString
    };

    public static final JSONWriter.Feature[] FASTJSON2_PRINT_FEATURES = new JSONWriter.Feature[]{
            JSONWriter.Feature.WriteMapNullValue,
            JSONWriter.Feature.WriteNullListAsEmpty,
            JSONWriter.Feature.WriteNullStringAsEmpty,
            JSONWriter.Feature.WriteEnumUsingToString
    };

    public static final JSONReader.Feature[] FASTJSON2_READER_FEATURES = new JSONReader.Feature[]{
            JSONReader.Feature.FieldBased,
            JSONReader.Feature.SupportArrayToBean,
            JSONReader.Feature.SupportClassForName
    };
}
