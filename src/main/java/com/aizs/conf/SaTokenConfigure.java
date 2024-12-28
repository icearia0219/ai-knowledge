package com.aizs.conf;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.context.SaHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 使用 SaHolder 获取请求对象
                    String requestUri = SaHolder.getRequest().getRequestPath();
                    // 登录验证
                    StpUtil.checkLogin();

                    // 检查权限并根据用户角色进行相应限制
                    // 工人只能查看自己的考核记录
                    if (requestUri.startsWith("/api/exam/records/")) {
                        // 如果路径是 /api/exam/records/{userid}，获取 userId 判断权限
                        String[] pathParts = requestUri.split("/");

                        // 判断路径是否包含 userId，排除类似 /api/exam/records/export 的路径
                        if (pathParts.length >= 5) {
                            // 只有路径符合 /api/exam/records/{userid} 这样的形式时，才进行 userId 比较
                            try {
                                Long userId = Long.parseLong(pathParts[pathParts.length - 1]);
                                if (StpUtil.getLoginIdAsLong() != userId) {
                                    // 非本人无法访问
                                    throw new RuntimeException("无权限访问");
                                }
                            } catch (NumberFormatException e) {
                                // 如果解析 userId 失败（如路径为 /export），则跳过该检查
                                // 这里可以加入日志或其他处理逻辑
                            }
                        }
                    }

// 管理员可以访问导出路径
                    String export = requestUri+"/export";
                    if (StpUtil.getSession().get("role").equals("admin")) {
                        System.out.println(export);
                        System.out.println(export.equals("/api/exam/records/export"));//true
                        System.out.println(export.matches("/api/exam/records/\\d+/export"));//false
                        if (export.equals("/api/exam/records/export") || export.matches("/api/exam/records/\\d+/export")) {
                            System.out.println("如果打印就是进语句了");
                            return; // 管理员可以访问导出功能
                        }
                    }
                    if (export.equals("/api/exam/records/export") || export.matches("/api/exam/records/\\d+/export")) {
                        Long userId = StpUtil.getLoginIdAsLong();
                        if (export.equals("/api/exam/records/export") || export.contains(String.valueOf(userId))) {
                            return; // 允许工人导出自己的记录
                        } else {
                            throw new RuntimeException("无权限访问"); // 其他人无法访问
                        }
                    }



                    // 管理员可以访问所有记录
//                    System.out.println(StpUtil.hasRole("admin"));
//                    if (StpUtil.hasRole("admin")) {
//                        // 判断管理员是否访问 /api/exam/records/export 或 /api/exam/records/{userid}/export
//                        if (requestUri.equals("/api/exam/records/export") || requestUri.matches("/api/exam/records/\\d+/export")) {
//                            return;
//                        }
//                    }

                    // 如果角色不是admin或worker，可以做更多的权限判断
                    // 如果你希望其他角色具有更严格的权限，可以在此处添加更多的判断逻辑
                }))
                .addPathPatterns("/**") // 所有请求都拦截
                .excludePathPatterns( // 排除不需要验证的请求路径
                        "/api/login",
                        "/api/register"
                );
    }
}
