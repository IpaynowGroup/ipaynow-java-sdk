package cn.ipaynow.sdk;

public enum SignTypeEnum {
    MD5("MD5"),
    RSA("RSA");

    private final String signType;

    SignTypeEnum(String signType) {
        this.signType = signType;
    }

    public String signType() {
        return signType;
    }
}
