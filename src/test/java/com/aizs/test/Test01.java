package com.aizs.test;

import cn.dev33.satoken.secure.BCrypt;

public class Test01 {
    public static void main(String[] args) {
        //加密
        String pw_hash = BCrypt.hashpw("123456", BCrypt.gensalt(12));
        System.out.println(pw_hash);
// 使用checkpw方法检查被加密的字符串是否与原始字符串匹配：
        boolean checkpw = BCrypt.checkpw("123456", pw_hash);

// gensalt方法提供了可选参数 (log_rounds) 来定义加盐多少，也决定了加密的复杂度:
//        String strong_salt = BCrypt.gensalt(10);
//        String stronger_salt = BCrypt.gensalt(12);
        System.out.println(checkpw);
//$2a$10$Rc7JQOHhFK86hefH4jWcd.s.C1DHKEF8bXeURpHoxmHOiDpbPeUJa
    }
}
