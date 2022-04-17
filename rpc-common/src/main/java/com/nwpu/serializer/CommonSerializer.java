package com.nwpu.serializer;

/**
 * 序列化接口
 * @author Junho
 * @date 2022/4/17 14:05
 */
public interface CommonSerializer {
    /**
     * TODO-目前只实现一个json
     */
    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;
    Integer HESSIAN_SERIALIZER = 2;
    Integer PROTOBUF_SERIALIZER = 3;

    Integer DEFAULT_SERIALIZER = JSON_SERIALIZER;

    static CommonSerializer getSerializer(Integer code){
        switch (code){
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();
}
