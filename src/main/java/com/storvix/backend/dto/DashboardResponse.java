package com.storvix.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardResponse {
    private Map<String, Object> storage;
    private Map<String, Object> totals;
    private List<Object> recentFiles;
    private List<Object> starred;
    private List<Object> recentActivity;
}
