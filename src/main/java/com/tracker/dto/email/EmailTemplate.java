package com.tracker.dto.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thymeleaf.context.Context;

import java.util.List;

/**
 * @author by Raj Aryan,
 * created on 06/10/2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplate {
    private String subject;
    private String from;
    private String to;
    private List<String> cc;
    private Context context;
    private String templateName;
}