package com.storvix.backend.dto;

import com.storvix.backend.entity.Share;
import com.storvix.backend.entity.ShareInvite;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ShareResponse {
    private String _id;
    private String role;
    private Boolean pending;
    private String kind;
    private String email;
    private LocalDateTime createdAt;
    private Object sharedWith; // Could be a map with name and email

    public static ShareResponse from(Share share) {
        return ShareResponse.builder()
                .pending(false)
                .kind("share")
                ._id(share.getId())
                .role(share.getRole())
                .createdAt(share.getCreatedAt())
                .sharedWith(java.util.Map.of(
                        "name", share.getSharedWith().getName(),
                        "email", share.getSharedWith().getEmail()
                ))
                .build();
    }

    public static ShareResponse from(ShareInvite invite) {
        return ShareResponse.builder()
                .pending(true)
                .kind("invite")
                ._id(invite.getId())
                .role(invite.getRole())
                .email(invite.getEmail())
                .createdAt(invite.getCreatedAt())
                .sharedWith(java.util.Map.of(
                        "name", invite.getEmail(),
                        "email", invite.getEmail()
                ))
                .build();
    }
}
