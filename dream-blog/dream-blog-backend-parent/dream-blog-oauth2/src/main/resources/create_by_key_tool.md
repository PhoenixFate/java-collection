## 公私钥对可以使用jdk的命令 keytool 来生成

生成密钥证书文件，每个证书包含公钥和私钥, 执行以下命令

```
keytool -genkeypair -alias oauth2 -keyalg RSA -keypass oauth2 -keystore oauth2.jks -storepass oauth2
```

别名为 oauth2，秘钥算法为 RSA，秘钥口令为 oauth2，秘钥库（文件）名称为 oauth2.jks，秘钥库（文件）口令为 oauth2。输入命令回车后，后面还问题需要回答，最后输入 y 表示确定

## 从ketStore中获取公钥

windows需要安装Win64OpenSSL_Light-1_1_1d.exe
mac直接安装openSSL

```
keytool -list -rfc --keystore oauth2.jks | openssl x509 -inform pem -pubkey
```