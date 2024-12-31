package com.duongw.common.config.i18n;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocalResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

//    @Override
//    public Locale resolveLocale(HttpServletRequest request) {
//        String languageHeader = request.getHeader("Accept-Language");
//        if (StringUtils.hasLength(languageHeader)) {
//            String[] languages = languageHeader.split(",");
//            for (String language : languages) {
//                Locale locale = Locale.forLanguageTag(language);
//                if (locale.getLanguage().equals("en")) {
//                    return locale;
//                }
//            }
//        }
//        else {
//            // default locale
//            return Locale.getDefault();
//        }
//        return Locale.getDefault();
//    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        // thuc hien lay gia tri request header

        String languageHeader = request.getHeader("Accept-Language");

        return StringUtils.hasLength(languageHeader) ?
                Locale.lookup(Locale.LanguageRange.parse(languageHeader), List.of(new Locale("en"), new Locale("fr"))) :
                Locale.getDefault();
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasename("i18n/messages");
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        rs.setCacheSeconds(3600);
        return rs;
    }
}
