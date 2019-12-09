package com.zhjs.scfcloud.file.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/7/24 14:25
 */
public class WebUtils {
    private static final Logger log = LoggerFactory.getLogger(WebUtils.class);

    public WebUtils() {
    }

    public static String getPathWithinApplication(HttpServletRequest request) {
        String contextPath = getContextPath(request);
        String requestUri = getRequestUri(request);
        if (StringUtils.startsWithIgnoreCase(requestUri, contextPath)) {
            String path = requestUri.substring(contextPath.length());
            return StringUtils.hasText(path) ? path : "/";
        } else {
            return requestUri;
        }
    }

    public static String getRequestUri(HttpServletRequest request) {
        String uri = (String)request.getAttribute("javax.servlet.include.request_uri");
        if (uri == null) {
            uri = request.getRequestURI();
        }

        return normalize(decodeAndCleanUriString(request, uri));
    }

    public static String normalize(String path) {
        return normalize(path, true);
    }

    private static String normalize(String path, boolean replaceBackSlash) {
        if (path == null) {
            return null;
        } else {
            String normalized = path;
            if (replaceBackSlash && path.indexOf(92) >= 0) {
                normalized = path.replace('\\', '/');
            }

            if (normalized.equals("/.")) {
                return "/";
            } else {
                if (!normalized.startsWith("/")) {
                    normalized = "/" + normalized;
                }

                while(true) {
                    int index = normalized.indexOf("//");
                    if (index < 0) {
                        while(true) {
                            index = normalized.indexOf("/./");
                            if (index < 0) {
                                while(true) {
                                    index = normalized.indexOf("/../");
                                    if (index < 0) {
                                        return normalized;
                                    }

                                    if (index == 0) {
                                        return null;
                                    }

                                    int index2 = normalized.lastIndexOf(47, index - 1);
                                    normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
                                }
                            }

                            normalized = normalized.substring(0, index) + normalized.substring(index + 2);
                        }
                    }

                    normalized = normalized.substring(0, index) + normalized.substring(index + 1);
                }
            }
        }
    }

    private static String decodeAndCleanUriString(HttpServletRequest request, String uri) {
        uri = decodeRequestString(request, uri);
        int semicolonIndex = uri.indexOf(59);
        return semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri;
    }

    public static String getContextPath(HttpServletRequest request) {
        String contextPath = (String)request.getAttribute("javax.servlet.include.context_path");
        if (contextPath == null) {
            contextPath = request.getContextPath();
        }

        contextPath = normalize(decodeRequestString(request, contextPath));
        if ("/".equals(contextPath)) {
            contextPath = "";
        }

        return contextPath;
    }


    public static String decodeRequestString(HttpServletRequest request, String source) {
        String enc = determineEncoding(request);

        try {
            return URLDecoder.decode(source, enc);
        } catch (UnsupportedEncodingException var4) {
            if (log.isWarnEnabled()) {
                log.warn("Could not decode request string [" + source + "] with encoding '" + enc + "': falling back to platform default encoding; exception message: " + var4.getMessage());
            }

            return URLDecoder.decode(source);
        }
    }

    protected static String determineEncoding(HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = "ISO-8859-1";
        }

        return enc;
    }

    public static boolean _isSessionCreationEnabled(ServletRequest request) {
        if (request != null) {
            Object val = request.getAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED);
            if (val != null && val instanceof Boolean) {
                return (Boolean)val;
            }
        }

        return true;
    }

    public static HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest)request;
    }

    public static HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse)response;
    }

    public static boolean isTrue(ServletRequest request, String paramName) {
        String value = getCleanParam(request, paramName);
        return value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("enabled") || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("on"));
    }

    public static String getCleanParam(ServletRequest request, String paramName) {
        return StringUtils.clean(request.getParameter(paramName));
    }

}
