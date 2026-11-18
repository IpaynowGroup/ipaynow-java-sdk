# iPayNow Java SDK

现在支付 Java SDK，封装统一下单、订单查询、退款、撤销、回调验签和前端表单提交字段生成。

## 环境要求

- JDK 8+
- Maven 3.6+

## Maven 坐标

```xml
<dependency>
    <groupId>io.github.ipaynowgroup</groupId>
    <artifactId>ipaynow-java-sdk</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 快速开始

```java
IpaynowClient client = new IpaynowClient(
        new IpaynowConfig("appId", "appKey", "https://pay.ipaynow.cn")
);

H5UnifiedOrderParams params = H5UnifiedOrderParams.builder()
        .mhtOrderNo("ORDER202607030001")
        .mhtOrderName("测试订单")
        .mhtOrderDetail("测试订单")
        .mhtOrderAmt(1L)
        .mhtOrderTimeOut(120L)
        .mhtOrderStartTime("20260703120000")
        .notifyUrl("https://example.com/notify")
        .frontNotifyUrl("https://example.com/front")
        .payChannelType("12")
        .outputType("1")
        .consumerCreateIp("127.0.0.1")
        .build();

H5UnifiedOrderResponse response = client.execute(new H5UnifiedOrderRequest(params));
if (response.success()) {
    String tn = response.getTn();
}
```

## 前端 Form 表单提交

需要让浏览器直接向现在支付网关提交表单时，使用 `buildFormFields` 生成完整请求字段。入参与 `execute` 一致，SDK 不发起 HTTP 请求。

```java
Map<String, String> fields = client.buildFormFields(new H5UnifiedOrderRequest(params));
```

返回字段包含业务参数、公共参数和签名字段，例如：

- `funcode`
- `version`
- `appId`
- `mhtCharset`
- `mhtSignType` 或 `signType`
- `mhtSignature`

表单提交地址由请求路径决定：

```java
String action = config.endpoint(new H5UnifiedOrderRequest(params).path());
```

## 支持接口

- `WP001`: 统一下单
- `MQ002`: 订单查询
- `N001`: 支付结果回调
- `R001`: 退款
- `Q001`: 退款查询
- `RN001`: 退款结果回调
- `R002`: 撤销
- `Q002`: 撤销查询

## 响应判断

- 正向同步接口默认使用 `responseCode == A001` 判断成功。
- 退款、撤销、退款查询、撤销查询使用 `responseCode == R000` 判断成功。
- 支付回调用 `transStatus == A001` 判断成功；部分通道（如支付宝 APP 支付）只下发 `tradeStatus`，此时回退读取 `tradeStatus`。
- 退款回调用 `tradeStatus == A001` 判断成功。

也可以直接读取原始字段：

```java
String code = response.code();
String message = response.message();
String rawBody = response.rawBody();
Map<String, String> fields = response.fields();
```

## 扩展字段

参数对象支持 `extraParams`。SDK 只禁止覆盖公共签名字段，业务字段不做强校验，避免网关协议扩展时 SDK 阻塞接入。

保留字段：

- `funcode`
- `version`
- `appId`
- `mhtCharset`
- `mhtSignType`
- `signType`
- `mhtSignature`
- `signature`

## 回调验签

```java
PaymentNotifyResponse notify = NotifyParser.parsePayment(body, appKey);
if (notify.success()) {
    String ack = NotifyAck.success();
}
```

退款回调：

```java
RefundNotifyResponse notify = NotifyParser.parseRefund(body, appKey);
```

## 发布产物

构建 jar、sources jar、javadoc jar：

```bash
mvn clean package
```

本地安装：

```bash
mvn clean install
```
