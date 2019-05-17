# SpirngBoot_JWT

## 组成部分

* 头部
> 存放描述信息，比如签名算法

* 载荷
> 可以存放自定义信息，一般用来存放用户名、用户角色、过期时间等
 
* 签名
> 签名 = HMACSHA256（头部 + 载荷 + 密钥），可以防止头部被篡改

需要注意的是，头部和载荷仅仅是base64编码，只有签名是加密的。
jwt校验的时候，会解密签名，得到签名里面的头部和载荷，再将 jwt中的头部和载荷进行对比。会得知此 jwt是否被篡改。

## 登陆接口

### 生成 jwt

```
eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4sQVVUSF9XUklURSIsInN1YiI6ImFkbWluIiwiZXhwIjoxNTU2ODc2NjA3fQ.UCAlDfvC4kgjTnBNz4UW48a0T9ouceLxC53_FvPhIwjh42IhpNN-JCg4XvAFzhRBwyXjl4p3jID6rIYSE_5_dA
```

## base64解码后的结果

```
    {
        "alg": "HS512"
    }
    {
        "authorities": "ROLE_ADMIN,AUTH_WRITE",
        "sub": "admin",
        "exp": 1556876607
    }
    P߼.$4焜Qn<kD瞯ܛτ#M$(8^ΔAå㗊w@
```
可以使用 `https://jwt.io/` 提供的解密方法

![jwt](https://github.com/xiazidong/markdown-img/blob/master/jwt.png?raw=true)

需要注意的是，secret是保存在服务端的，jwt的签发和生成也是在服务端；
secret用来进行 jwt的签发和校验，所以算是私钥，如果客户端知道了这个私钥，那么就可以自我签发 jwt。

## 处理 filter中的异常
filter中的异常无法被 springboot统一异常处理类所捕获，需要特殊处理
实现 `ErrorController`接口，重写 `getErrorPath()`，强制转跳到设定好到 url中，
然后再写一个 handler去处理这个异常。

```java
@Controller
public class CusErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    @ResponseBody
    public void error(HttpServletRequest request){
        // 先处理404异常
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == HttpServletResponse.SC_NOT_FOUND){
            throw new BusinessException("资源未找到");
        }
        Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (t != null) {
          if (t instanceof MalformedJwtException) {
                     throw new BusinessException("jwt格式不正确");
                 }
                 t.printStackTrace();
                 throw new BusinessException(t.getMessage());
        }
        // todo 处理其他异常
    }
}
```
