package com.tracker.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;

/**
 * @author Pawan Saini
 * Created on 14/12/23.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginationResponse implements Serializable {

    @JsonProperty(value = "current_page")
    private Integer currentPage;

    @JsonProperty(value = "page_size")
    private Integer pageSize;

    @JsonProperty(value = "total_pages")
    private Integer totalPages;

    @JsonProperty(value = "total_count")
    private Integer totalCount;

    public PaginationResponse(Page<?> page) {
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalCount = (int) page.getTotalElements();
    }
}
