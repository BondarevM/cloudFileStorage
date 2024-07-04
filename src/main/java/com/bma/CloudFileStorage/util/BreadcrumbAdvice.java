package com.bma.CloudFileStorage.util;

import com.bma.CloudFileStorage.models.Breadcrumb;
import com.bma.CloudFileStorage.services.BreadcrumbService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
@ControllerAdvice
public class BreadcrumbAdvice {
    private final BreadcrumbService breadcrumbService;

    public BreadcrumbAdvice(BreadcrumbService breadcrumbService) {
        this.breadcrumbService = breadcrumbService;
    }

    @ModelAttribute("breadcrumbs")
    public List<Breadcrumb> populateBreadcrumbs(HttpServletRequest request) {
        return breadcrumbService.generateBreadcrumbs(request);
    }
}
