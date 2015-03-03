JavaSimpleProxy
======
非常简单的翻墙程序。
工作原理
------
这个程序并没有实现socks代理，socks代理由Dante ( A free SOCKS server )来完成。

这个程序只是转发浏览器的包到Dante，由Dante去访问网站，再把Dante的包转发给浏览器。

在转发时对数据进行异或加密来实现翻墙。

代码很简陋，但是确实能工作。

　　　PC　　　　　　　　　　　　　　　VPS<br/>
[　　　　　　　　　　　　]　　　　[　　　　　　　　　　]<br/>
[ 　浏览器 <-> Proxy.java　]<---墙-->[　Proxy.java<->Dante ]　<----->　网站<br/>
[　　　　　　　　　　　　]　　　　[　　　　　　　　　　]
用法
------
###在VPS上
    java javasimpleproxy.Proxy -s -l 30001 -p 30000
在端口30001监听，连接本地Dante的30000端口。
###在PC上
    java javasimpleproxy.Proxy -c 106.187.95.66 -l 7202  -p 30001
在端口7202监听，连接106.187.95.66的30001端口。