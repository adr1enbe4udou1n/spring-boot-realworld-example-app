package io.okami101.realworld.core.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FollowerUser {
    private int followingId;
    private int followerId;
}
