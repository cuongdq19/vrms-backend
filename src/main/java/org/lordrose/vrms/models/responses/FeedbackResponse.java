package org.lordrose.vrms.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponse {

    private Long id;
    private Integer ratings;
    private String content;
    private String imageUrls;
    private Long userId;
    private String fullName;
    private String userImageUrl;
    private Long requestId;
}
