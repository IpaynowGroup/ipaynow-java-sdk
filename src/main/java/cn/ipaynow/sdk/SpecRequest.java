package cn.ipaynow.sdk;

import java.util.Map;

public class SpecRequest<T extends IpaynowResponse> implements IpaynowRequest<T> {
    private final String funcode;
    private final String version;
    private final String mhtCharset;
    private final RequestParams params;
    private final Class<T> responseClass;
    private final String signTypeField;
    private final String path;
    private final boolean excludeSignType;


    public SpecRequest(String funcode, String version, String mhtCharset, RequestParams params, Class<T> responseClass, String signTypeField) {
        this(funcode, version, mhtCharset, params, responseClass, signTypeField, "/", false);
    }

    public SpecRequest(String funcode, String version, String mhtCharset, RequestParams params, Class<T> responseClass, String signTypeField, String path, boolean excludeSignType) {
        this.funcode = funcode;
        this.version = version;
        this.mhtCharset = mhtCharset;
        this.params = params;
        this.responseClass = responseClass;
        this.signTypeField = signTypeField;
        this.path = path;
        this.excludeSignType = excludeSignType;
    }

    @Override
    public String funcode() {
        return this.funcode;
    }

    @Override
    public String version() {
        return this.version;
    }

    @Override
    public String mhtCharset() {
        return this.mhtCharset;
    }

    public Map<String, String> params() {
        return params.toMap();
    }

    public Class<T> responseClass() {
        return responseClass;
    }

    public String signTypeField() {
        return signTypeField;
    }

    public String signatureField() {
        return "mhtSignature";
    }

    public String responseSignatureField() {
        return "signature";
    }

    public String path() {
        return path;
    }

    public boolean excludeSignType() {
        return excludeSignType;
    }
}
